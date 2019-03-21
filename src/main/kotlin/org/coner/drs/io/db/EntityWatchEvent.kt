package org.coner.drs.io.db

import io.reactivex.functions.Consumer
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent
import java.util.*
import kotlin.reflect.KProperty1

data class EntityWatchEvent<E : WatchedEntity<E>>(val watchEvent: WatchEvent<*>, val id: UUID, val entity: E?)

fun <E : WatchedEntity<E>> entityWatchEventConsumer(
        idProperty: KProperty1<E, UUID>,
        list: MutableList<E>
) = Consumer<EntityWatchEvent<E>> { event ->
    val entity = event.entity
    val kind = event.watchEvent.kind()
    if (entity != null &&
            (kind == StandardWatchEventKinds.ENTRY_CREATE
                    || kind == StandardWatchEventKinds.ENTRY_MODIFY
            )
    ) {
        val index = list.indexOfFirst { idProperty.get(it) == event.id }
        if (index >= 0) {
            list[index].onWatchedEntityUpdate(entity)
        } else {
            list.add(entity)
        }
    } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
        val id = event.id
        val index = list.indexOfFirst { idProperty.get(it) == id }
        if (index >= 0) {
            list.removeAt(index)
        }
    }
}

interface WatchedEntity<E> {
    fun onWatchedEntityUpdate(updated: E)
}
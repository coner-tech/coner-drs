package org.coner.drs.io.db

import io.reactivex.functions.Consumer
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent
import java.util.*
import kotlin.reflect.KProperty1

data class EntityWatchEvent <T : Any>(val watchEvent: WatchEvent<*>, val id: UUID, val entity: T?)

fun <T : Any> entityWatchEventConsumer(
        idProperty: KProperty1<T, UUID>,
        list: MutableList<T>
) = Consumer<EntityWatchEvent<T>> { event ->
    val entity = event.entity
    val kind = event.watchEvent.kind()
    if (entity != null &&
            (kind == StandardWatchEventKinds.ENTRY_CREATE
                    || kind == StandardWatchEventKinds.ENTRY_MODIFY
            )
    ) {
        val index = list.indexOfFirst { idProperty.get(it) == event.id }
        if (index >= 0) {
            list[index] = entity
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
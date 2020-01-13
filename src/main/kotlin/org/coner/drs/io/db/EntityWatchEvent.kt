/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.coner.drs.io.db

import io.reactivex.functions.Consumer
import org.coner.snoozle.db.entity.EntityEvent
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent
import java.util.*
import kotlin.reflect.KProperty1

data class EntityWatchEvent<E : WatchedEntity<E>>(val entityEvent: EntityEvent<*>, val id: UUID, val entity: E?)

fun <E : WatchedEntity<E>> entityWatchEventConsumer(
        idProperty: KProperty1<E, UUID>,
        list: MutableList<E>
) = Consumer<EntityWatchEvent<E>> { event ->
    val entity = event.entity
    val state = event.entityEvent.state
    if (entity != null && state == EntityEvent.State.EXISTS) {
        val index = list.indexOfFirst { idProperty.get(it) == event.id }
        if (index >= 0) {
            list[index].onWatchedEntityUpdate(entity)
        } else {
            list.add(entity)
        }
    } else if (state == EntityEvent.State.DELETED) {
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
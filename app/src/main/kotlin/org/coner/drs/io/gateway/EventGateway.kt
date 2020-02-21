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

package org.coner.drs.io.gateway

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.mapper.EventMapper
import org.coner.drs.io.DrsIoController
import org.coner.drs.io.db.EntityWatchEvent
import org.coner.drs.node.db.entity.EventDbEntity
import org.coner.drs.node.db.getEvent
import tornadofx.*

class EventGateway : Controller() {

    val io: DrsIoController by inject()
    private val db = io.model.db!!
    val mapper by lazy { EventMapper(io.model.pathToCrispyFishDatabase!!) }

    fun list(): List<Event> {
        return db.entity<EventDbEntity>().list().map { mapper.toUiEntity(it)!! }
    }

    fun asRunEvent(event: Event): RunEvent? {
        val dbEntity = db.entity<EventDbEntity>().getEvent(event.id)
        return mapper.toRunEventUiEntity(dbEntity)
    }

    fun save(event: Event) {
        db.entity<EventDbEntity>().put(mapper.toDbEntity(event))
    }

    fun watchList(): Observable<EntityWatchEvent<Event>> = db.entity<EventDbEntity>().watchListing()
            .subscribeOn(Schedulers.io())
            .map { EntityWatchEvent(
                    entityEvent = it,
                    id = it.id,
                    entity = mapper.toUiEntity(it.entity)
            ) }

}


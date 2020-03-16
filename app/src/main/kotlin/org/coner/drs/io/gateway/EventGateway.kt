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
import org.coner.drs.di.katanaScopes
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.mapper.EventMapper
import org.coner.drs.node.payload.EntityWatchEvent
import org.coner.drs.node.service.EventService
import org.rewedigital.katana.KatanaTrait
import tornadofx.*

class EventGateway : Controller(), KatanaTrait {

    override val component = katanaScopes.home.component

    private val service: EventService by component.inject()
    private val mapper: EventMapper by component.inject()

    fun list(): List<Event> {
        return service.list()
                .mapNotNull { mapper.toRunEventUiEntity(it) }
    }

    fun asRunEvent(event: Event): RunEvent? {
        val dbEntity = service.findEventById(event.id)
        return mapper.toRunEventUiEntity(dbEntity)
    }

    fun save(event: Event) {
        val dbEntity = mapper.toDbEntity(event)
        service.save(dbEntity)
    }

    fun watchList(): Observable<EntityWatchEvent<Event>> {
        return service.watchList()
                .subscribeOn(Schedulers.io())
                .map {
                    EntityWatchEvent(
                            entityEvent = it.entityEvent,
                            id = it.id,
                            entity = mapper.toUiEntity(it.entity)
                    )
                }
    }

}


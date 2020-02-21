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
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.mapper.RunMapper
import org.coner.drs.io.DrsIoController
import org.coner.drs.io.db.EntityWatchEvent
import org.coner.drs.node.db.entity.RunDbEntity
import tornadofx.*

class RunGateway : Controller() {

    val io: DrsIoController by inject(FX.defaultScope)
    private val db = io.model.db!!
    private val eventGateway: EventGateway by inject(FX.defaultScope)

    fun list(event: Event): Single<List<Run>> = Single.fromCallable { db.entity<RunDbEntity>().list(
            eventGateway.mapper.toDbEntity(event).id
    ).map {
        RunMapper.toUiEntity(event = event, dbEntity = it) as Run
    } }

    fun save(run: Run) {
        db.entity<RunDbEntity>().put(RunMapper.toDbEntity(run))
    }

    fun watchList(event: Event, registrations: List<Registration>): Observable<EntityWatchEvent<Run>> {
        return db.entity<RunDbEntity>().watchListing(eventGateway.mapper.toDbEntity(event).id)
                .subscribeOn(Schedulers.io())
                .map { EntityWatchEvent(
                        entityEvent = it,
                        id = it.id,
                        entity = RunMapper.toUiEntity(event, it.entity).apply {
                            if (this != null) hydrateWithRegistrationMetadata(this, registrations)
                        }
                ) }
    }

    fun delete(run: Run) {
        db.entity<RunDbEntity>().delete(RunMapper.toDbEntity(run))
    }

    /* refactor to service */
    fun hydrateWithRegistrationMetadata(runs: List<Run>, registrations: List<Registration>, destructive: Boolean = false) {
        for (run in runs) {
            hydrateWithRegistrationMetadata(run, registrations, destructive)
        }
    }

    /* refactor to service */
    fun hydrateWithRegistrationMetadata(run: Run, registrations: List<Registration>, destructive: Boolean = false) {
        val category = run.registration?.category
        val handicap = run.registration?.handicap
        val number = run.registration?.number
        if (category == null || handicap == null || number == null) return
        val hydratedRegistration = registrations.firstOrNull {
            it.category == category
            && it.handicap == handicap
            && it.number == number
        }
        run.registration = when {
            hydratedRegistration != null -> hydratedRegistration
            destructive -> Registration(category = category, handicap = handicap, number = number)
            else -> return
        }
    }

}

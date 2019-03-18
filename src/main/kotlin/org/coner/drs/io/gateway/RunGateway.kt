package org.coner.drs.io.gateway

import io. reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.Run
import org.coner.drs.io.DrsIoController
import org.coner.drs.io.db.EntityWatchEvent
import org.coner.drs.io.db.entity.RunDbEntity
import org.coner.drs.domain.mapper.RunMapper
import org.coner.snoozle.db.jvm.watchListing
import tornadofx.*

class RunGateway : Controller() {

    val io: DrsIoController by inject(FX.defaultScope)
    private val db = io.model.db!!
    private val eventGateway: EventGateway by inject(FX.defaultScope)

    fun list(event: Event): Single<List<Run>> = Single.fromCallable { db.list(
            RunDbEntity::eventId to eventGateway.mapper.toDbEntity(event).id
    ).map {
        RunMapper.toUiEntity(event = event, dbEntity = it) as Run
    } }

    fun save(run: Run) {
        db.put(RunMapper.toDbEntity(run))
    }

    fun watchList(event: Event, registrations: List<Registration>): Observable<EntityWatchEvent<Run>> {
        return db.watchListing(RunDbEntity::eventId to eventGateway.mapper.toDbEntity(event).id)
                .subscribeOn(Schedulers.io())
                .map { EntityWatchEvent(
                        watchEvent = it.watchEvent,
                        id = it.id,
                        entity = RunMapper.toUiEntity(event, it.entity).apply {
                            if (this != null) hydrateWithRegistrationMetadata(this, registrations)
                        }
                ) }
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

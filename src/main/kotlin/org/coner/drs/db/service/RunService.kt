package org.coner.drs.db.service

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.coner.drs.Event
import org.coner.drs.Run
import org.coner.drs.db.DrsIoController
import org.coner.drs.db.EntityWatchEvent
import org.coner.drs.db.entity.EventDbEntityMapper
import org.coner.drs.db.entity.RunDbEntity
import org.coner.drs.db.entity.RunDbEntityMapper
import org.coner.snoozle.db.jvm.watchListing
import tornadofx.*

class RunService : Controller() {

    val io: DrsIoController by inject(FX.defaultScope)
    private val db = io.model.db!!

    fun list(event: Event): List<Run> = db.list(
            RunDbEntity::eventId to EventDbEntityMapper.toDbEntity(event).id
    ).map { RunDbEntityMapper.toUiEntity(event = event, dbEntity = it) as Run }

    fun save(run: Run) {
        db.put(RunDbEntityMapper.toDbEntity(run))
    }

    fun watchList(event: Event): Observable<EntityWatchEvent<Run>> {
        return db.watchListing(RunDbEntity::eventId to EventDbEntityMapper.toDbEntity(event).id)
                .subscribeOn(Schedulers.io())
                .map { EntityWatchEvent(
                        watchEvent = it.watchEvent,
                        id = it.id,
                        entity = RunDbEntityMapper.toUiEntity(event, it.entity)
                ) }
    }
}
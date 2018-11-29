package org.coner.drs.db.service

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.coner.drs.Event
import org.coner.drs.db.DrsIoController
import org.coner.drs.db.EntityWatchEvent
import org.coner.drs.db.entity.EventDbEntity
import org.coner.drs.db.entity.toDbEntity
import org.coner.drs.db.entity.toUiEntity
import org.coner.snoozle.db.jvm.watchListing
import tornadofx.*

class EventService : Controller() {

    val io: DrsIoController by inject()
    private val db = io.db!!

    fun list(): List<Event> = db.list<EventDbEntity>()
            .map { toUiEntity(it) as Event }

    fun save(event: Event) {
        db.put(toDbEntity(event))
    }

    fun watchList(): Observable<EntityWatchEvent<Event>>  = db.watchListing<EventDbEntity>()
            .subscribeOn(Schedulers.io())
            .map { EntityWatchEvent(
                    watchEvent = it.watchEvent,
                    id = it.id,
                    entity = toUiEntity(it.entity)
            ) }
}
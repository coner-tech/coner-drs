package org.coner.drs.io.db.service

import io. reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.coner.drs.Event
import org.coner.drs.Run
import org.coner.drs.io.DrsIoController
import org.coner.drs.io.db.EntityWatchEvent
import org.coner.drs.io.db.entity.EventDbEntity
import org.coner.drs.io.db.entity.EventDbEntityMapper
import org.coner.drs.io.db.entity.RunDbEntity
import org.coner.drs.io.db.entity.RunDbEntityMapper
import org.coner.snoozle.db.jvm.watchListing
import tornadofx.*
import java.math.BigDecimal

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

    fun addTimeToFirstRunInSequenceWithoutRawTime(event: Event, time: BigDecimal) {
        val eventDbEntity = EventDbEntityMapper.toDbEntity(event)
        val firstRunInSequenceWithoutTimeOptional = db.list(
                RunDbEntity::eventId to eventDbEntity.id
        ).parallelStream()
                .sorted(compareBy(RunDbEntity::sequence))
                .filter { it.rawTime == null }
                .findFirst()
        if (firstRunInSequenceWithoutTimeOptional.isPresent) {
            val runWithTime = firstRunInSequenceWithoutTimeOptional.get().copy(rawTime = time)
            db.put(runWithTime)
        } else {
            // Consider error reporting for this scenario.
            // There has possibly been a false finish trip, or perhaps a car managed to stage and launch without
            // being noticed by Timing workers. Recommend to hold start while resolving situation.
            val lastRunInSequenceWithTime = findLastRunInSequenceWithTime(eventDbEntity)
            val addRunWithTime = RunDbEntity(
                    eventId = eventDbEntity.id,
                    sequence = lastRunInSequenceWithTime?.sequence?.plus(1) ?: 1,
                    rawTime = time
            )
            db.put(addRunWithTime)
        }
    }

    private fun findLastRunInSequenceWithTime(eventDbEntity: EventDbEntity): RunDbEntity? = db.list(
            RunDbEntity::eventId to eventDbEntity.id
    ).parallelStream()
            .sorted(compareByDescending(RunDbEntity::sequence))
            .filter { it.rawTime != null }
            .findFirst().orElse(null)

    fun insertNextDriver(addNextDriver: Run) {
        val addNextDriverDbEntity = RunDbEntityMapper.toDbEntity(addNextDriver)
        val runDbEntity = db.list(
                RunDbEntity::eventId to addNextDriverDbEntity.eventId
        ).parallelStream()
                .filter { it.sequence == addNextDriver.sequence }
                .findFirst()
                .orElse(RunDbEntity(
                        eventId = addNextDriverDbEntity.eventId,
                        sequence = addNextDriverDbEntity.sequence
                ))
                .copy(
                        category = addNextDriverDbEntity.category,
                        handicap = addNextDriverDbEntity.handicap,
                        number = addNextDriverDbEntity.number
                )
        db.put(runDbEntity)
    }

}

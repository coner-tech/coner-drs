package org.coner.drs.domain.mapper

import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.Run
import org.coner.drs.io.db.entity.RunDbEntity

object RunMapper {
    fun toUiEntity(event: Event, dbEntity: RunDbEntity?) = if (dbEntity != null) Run(
            id = dbEntity.id,
            event = event,
            sequence = dbEntity.sequence,
            registration = if (dbEntity.handicap.isNotBlank() && dbEntity.number.isNotBlank())
                Registration(
                    category = dbEntity.category,
                    handicap = dbEntity.handicap,
                    number = dbEntity.number
                )
            else
                null,
            rawTime = dbEntity.rawTime,
            cones = dbEntity.cones,
            didNotFinish = dbEntity.didNotFinish,
            disqualified = dbEntity.disqualified,
            rerun = dbEntity.rerun
    ) else null

    fun toDbEntity(uiRun: Run) = RunDbEntity(
            id = uiRun.id,
            eventId = uiRun.event.id,
            sequence = uiRun.sequence,
            category = uiRun.registration?.category ?: "",
            handicap = uiRun.registration?.handicap ?: "",
            number = uiRun.registration?.number ?: "",
            rawTime = uiRun.rawTime,
            cones = uiRun.cones,
            didNotFinish = uiRun.didNotFinish,
            disqualified = uiRun.disqualified,
            rerun = uiRun.rerun
    )

    fun copy(uiRun: Run) = Run(
            id = uiRun.id,
            event = uiRun.event,
            sequence = uiRun.sequence,
            registration = uiRun.registration,
            rawTime = uiRun.rawTime,
            cones = uiRun.cones,
            didNotFinish = uiRun.didNotFinish,
            disqualified = uiRun.disqualified,
            rerun = uiRun.rerun
    )
}
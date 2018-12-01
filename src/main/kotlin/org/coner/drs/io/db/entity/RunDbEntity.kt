package org.coner.drs.io.db.entity

import org.coner.drs.Event
import org.coner.drs.Run
import org.coner.snoozle.db.Entity
import org.coner.snoozle.db.EntityPath
import java.math.BigDecimal
import java.util.*

@EntityPath("/events/{eventId}/runs/{id}")
data class RunDbEntity(
        val id: UUID = UUID.randomUUID(),
        val eventId: UUID,
        val sequence: Int,
        val category: String = "",
        val handicap: String = "",
        val number: String = "",
        val rawTime: BigDecimal? = null,
        val cones: Int = 0,
        val didNotFinish: Boolean = false,
        val disqualified: Boolean = false,
        val rerun: Boolean = false
) : Entity

object RunDbEntityMapper {
    fun toUiEntity(event: Event, dbEntity: RunDbEntity?) = if (dbEntity != null) Run(
            id = dbEntity.id,
            event = event,
            sequence = dbEntity.sequence,
            category = dbEntity.category,
            handicap = dbEntity.handicap,
            number = dbEntity.number,
            rawTime = dbEntity.rawTime,
            cones = dbEntity.cones,
            didNotFinish = dbEntity.didNotFinish,
            disqualified = dbEntity.disqualified,
            rerun = dbEntity.rerun
    ) else null

    fun toDbEntity(uiEntity: Run) = RunDbEntity(
            id = uiEntity.id,
            eventId = uiEntity.event.id,
            sequence = uiEntity.sequence,
            category = uiEntity.category,
            handicap = uiEntity.handicap,
            number = uiEntity.number,
            rawTime = uiEntity.rawTime,
            cones = uiEntity.cones,
            didNotFinish = uiEntity.didNotFinish,
            disqualified = uiEntity.disqualified,
            rerun = uiEntity.rerun
    )
}

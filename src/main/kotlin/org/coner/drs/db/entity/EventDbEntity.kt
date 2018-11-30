package org.coner.drs.db.entity

import org.coner.drs.Event
import org.coner.snoozle.db.Entity
import org.coner.snoozle.db.EntityPath
import java.time.LocalDate
import java.util.*

@EntityPath("/events/{id}")
data class EventDbEntity(
        val id: UUID = UUID.randomUUID(),
        val date: LocalDate,
        val name: String
) : Entity

object EventDbEntityMapper {

    fun toUiEntity(dbEntity: EventDbEntity?) = if (dbEntity != null) Event(
            id = dbEntity.id,
            date = dbEntity.date,
            name = dbEntity.name
    ) else null

    fun toDbEntity(uiEntity: Event) = EventDbEntity(
            id = uiEntity.id,
            date = uiEntity.date,
            name = uiEntity.name
    )

}

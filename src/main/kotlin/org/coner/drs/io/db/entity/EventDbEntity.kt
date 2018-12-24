package org.coner.drs.io.db.entity

import org.coner.drs.Event
import org.coner.snoozle.db.Entity
import org.coner.snoozle.db.EntityPath
import java.io.File
import java.time.LocalDate
import java.util.*

@EntityPath("/events/{id}")
data class EventDbEntity(
        val id: UUID = UUID.randomUUID(),
        val date: LocalDate,
        val name: String,
        val crispyFishMetadata: CrispyFishMetadata

) : Entity {
    data class CrispyFishMetadata(
            val classDefinitionFile: String,
            val eventControlFile: String
    )
}

class EventDbEntityMapper(
        val crispyFishDatabase: File
) {

    fun toUiEntity(dbEntity: EventDbEntity?) = if (dbEntity != null) Event(
                id = dbEntity.id,
                date = dbEntity.date,
                name = dbEntity.name,
                crispyFishMetadata = with(dbEntity.crispyFishMetadata) {
                    Event.CrispyFishMetadata(
                            classDefinitionFile = crispyFishDatabase.resolve(classDefinitionFile),
                            eventControlFile = crispyFishDatabase.resolve(eventControlFile)
                    )
                }
        )
    else null

    fun toDbEntity(uiEntity: Event) = EventDbEntity(
            id = uiEntity.id,
            date = uiEntity.date,
            name = uiEntity.name,
            crispyFishMetadata = with(uiEntity.crispyFishMetadata) {
                EventDbEntity.CrispyFishMetadata(
                        classDefinitionFile = classDefinitionFile.toRelativeString(crispyFishDatabase),
                        eventControlFile = eventControlFile.toRelativeString(crispyFishDatabase)
                )
            }
        )

}

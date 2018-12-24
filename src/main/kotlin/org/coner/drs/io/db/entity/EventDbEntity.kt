package org.coner.drs.io.db.entity

import org.coner.crispyfish.filetype.classdefinition.ClassDefinitionFile
import org.coner.crispyfish.filetype.ecf.EventControlFile
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
        val crispyFishClassDefinitionFile: String,
        val crispyFishEventControlFile: String
) : Entity

class EventDbEntityMapper(
        val crispyFishDatabase: File
) {

    fun toUiEntity(dbEntity: EventDbEntity?) = if (dbEntity != null) {
        val classDefinitionFile = ClassDefinitionFile(
                file = crispyFishDatabase.resolve(dbEntity.crispyFishClassDefinitionFile)
        )
        Event(
                id = dbEntity.id,
                date = dbEntity.date,
                name = dbEntity.name,
                classDefinitionFile = classDefinitionFile,
                eventControlFile = EventControlFile(
                        file = crispyFishDatabase.resolve(dbEntity.crispyFishEventControlFile),
                        classDefinitionFile = classDefinitionFile,
                        conePenalty = 2,
                        isTwoDayEvent = false
                )
        )
    } else null

    fun toDbEntity(uiEntity: Event) = EventDbEntity(
            id = uiEntity.id,
            date = uiEntity.date,
            name = uiEntity.name,
            crispyFishClassDefinitionFile = uiEntity.classDefinitionFile.file.toRelativeString(crispyFishDatabase),
            crispyFishEventControlFile = uiEntity.eventControlFile.file.toRelativeString(crispyFishDatabase)
    )

}

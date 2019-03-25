package org.coner.drs.domain.mapper

import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.io.db.entity.EventDbEntity
import java.io.File

class EventMapper(
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

    fun toRunEventUiEntity(dbEntity: EventDbEntity?) = if (dbEntity != null) RunEvent(
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
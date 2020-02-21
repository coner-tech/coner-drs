/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.coner.drs.domain.mapper

import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.node.db.entity.EventDbEntity
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
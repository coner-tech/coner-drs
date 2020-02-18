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

package org.coner.drs.io.db.entity

import org.coner.snoozle.db.entity.Entity
import java.time.LocalDate
import java.util.*

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


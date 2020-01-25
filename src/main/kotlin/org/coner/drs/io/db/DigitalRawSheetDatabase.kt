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

package org.coner.drs.io.db

import org.coner.drs.io.db.blob.EventReport
import org.coner.drs.io.db.entity.EventDbEntity
import org.coner.drs.io.db.entity.RunDbEntity
import org.coner.snoozle.db.Database
import org.coner.snoozle.db.entity.EntityResource
import org.coner.snoozle.db.versioning.EntityVersioningStrategy
import java.nio.file.Path
import java.util.*

class DigitalRawSheetDatabase(
        root: Path
) : Database(root) {

    override val types = registerTypes {
        entity<EventDbEntity> {
            path = "events" / { it.id } + ".json"
        }
        entity<RunDbEntity> {
            path = "events" / { it.eventId } / "runs" / { it.id } + ".json"
            versioning = EntityVersioningStrategy.AutomaticInternalVersioning
        }
        blob<EventReport> {
            path = "events" / { it.eventId } / "reports" / string { it.name } + "." + string { it.extension }
        }
    }
}

fun EntityResource<EventDbEntity>.getEvent(id: UUID) = get(id)
fun EntityResource<RunDbEntity>.getRun(runId: UUID, id: UUID) = get(runId, id)
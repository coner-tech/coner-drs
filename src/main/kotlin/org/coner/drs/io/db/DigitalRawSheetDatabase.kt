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
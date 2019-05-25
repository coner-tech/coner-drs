package org.coner.drs.io.db

import org.coner.drs.io.db.entity.EventDbEntity
import org.coner.drs.io.db.entity.RunDbEntity
import org.coner.snoozle.db.Database
import java.nio.file.Path

class DigitalRawSheetDatabase(
        root: Path
) : Database(root) {

    override val entities = listOf(
            entityDefinition<EventDbEntity>(),
            entityDefinition<RunDbEntity>()
    )
}
package org.coner.drs.node.payload

import org.coner.drs.node.db.entity.RunDbEntity
import java.util.*

class AlterDriverSequenceResult(
        val runs: List<RunDbEntity>,
        val insertedRunId: UUID,
        val shiftedRunIds: Set<UUID>
)
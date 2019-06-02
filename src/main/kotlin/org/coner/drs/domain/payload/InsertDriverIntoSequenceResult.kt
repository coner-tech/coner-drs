package org.coner.drs.domain.payload

import org.coner.drs.domain.entity.Run
import java.util.*

class InsertDriverIntoSequenceResult(
        val runs: List<Run>,
        val insertRunId: UUID,
        val shiftRunIds: Set<UUID>
)
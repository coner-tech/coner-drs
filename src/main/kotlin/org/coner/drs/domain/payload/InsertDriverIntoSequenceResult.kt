package org.coner.drs.domain.payload

import org.coner.drs.domain.entity.Run

class InsertDriverIntoSequenceResult(
        val runs: List<Run>,
        val insertRun: Run,
        val shiftRuns: Array<Run>
)
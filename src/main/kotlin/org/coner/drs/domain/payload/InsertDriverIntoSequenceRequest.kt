package org.coner.drs.domain.payload

import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.Run

class InsertDriverIntoSequenceRequest(
        val event: Event,
        val runs: List<Run>,
        val sequence: Int,
        val relative: Relative,
        val registration: Registration?,
        val dryRun: Boolean = false
) {

    enum class Relative {
        BEFORE,
        AFTER
    }
}
package org.coner.drs.node.payload

import java.util.*

class AlterDriverSequenceRequest(
        val eventId: UUID,
        val sequence: Int,
        val relative: Relative,
        val category: String,
        val handicap: String,
        val number: String,
        val dryRun: Boolean = false
) {
    enum class Relative {
        BEFORE,
        AFTER
    }
}
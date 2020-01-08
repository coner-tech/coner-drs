package org.coner.drs.io.db.blob

import org.coner.snoozle.db.blob.Blob
import java.util.*

data class EventReport(
        val eventId: UUID,
        val name: String,
        val extension: String
) : Blob
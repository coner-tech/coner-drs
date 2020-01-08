package org.coner.drs.domain.mapper

import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.TextReport
import org.coner.drs.io.db.blob.EventReport

object ReportMapper {
    fun toAuditList(event: Event) = EventReport(
            eventId = event.id,
            name = "audit-list",
            extension = "html"
    )
}
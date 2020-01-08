package org.coner.drs.io.gateway

import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.TextReport
import org.coner.drs.domain.mapper.ReportMapper
import org.coner.drs.io.DrsIoController
import org.coner.drs.io.db.blob.EventReport
import tornadofx.*
import java.nio.file.Path

class EventReportGateway : Controller() {

    val io: DrsIoController by inject(FX.defaultScope)
    private val db = requireNotNull(io.model.db)

    fun saveAuditList(auditList: TextReport.AuditList) {
        val report = ReportMapper.toAuditList(auditList.event)
        db.blob<EventReport>().put(report, auditList.content)
    }

    fun pathTo(event: Event): Path {
        val report = ReportMapper.toAuditList(event)
        return db.blob<EventReport>().getAbsolutePathTo(report)
    }
}
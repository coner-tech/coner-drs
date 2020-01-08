package org.coner.drs.domain.service

import org.coner.drs.di.katanaAppComponent
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.entity.TextReport
import org.coner.drs.io.gateway.EventReportGateway
import org.coner.drs.report.AuditListReport
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import tornadofx.*

class ReportService : Controller(), KatanaTrait {

    override val component = Component(katanaAppComponent)

    private val gateway: EventReportGateway by inject()
    private val auditList: AuditListReport by component.inject()

    fun generateAuditList(runEvent: RunEvent) {
        val content = auditList.render(runEvent)
        val report = TextReport.AuditList(runEvent, content)
        gateway.saveAuditList(report)
    }
}
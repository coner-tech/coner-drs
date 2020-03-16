/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.coner.drs.io.gateway

import org.coner.drs.di.katanaScopes
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.TextReport
import org.coner.drs.domain.mapper.ReportMapper
import org.coner.drs.node.db.DigitalRawSheetDatabase
import org.coner.drs.node.db.blob.EventReport
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import tornadofx.*
import java.nio.file.Path

class EventReportGateway : Controller(), KatanaTrait {

    override val component: Component = katanaScopes.runEvent.component

    private val db: DigitalRawSheetDatabase by component.inject()

    fun saveAuditList(auditList: TextReport.AuditList) {
        val report = ReportMapper.toAuditList(auditList.event)
        db.blob<EventReport>().put(report, auditList.content)
    }

    fun pathTo(event: Event): Path {
        val report = ReportMapper.toAuditList(event)
        return db.blob<EventReport>().getAbsolutePathTo(report)
    }
}
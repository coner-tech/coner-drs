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

package org.coner.drs.report

import kotlinx.html.*
import kotlinx.html.stream.createHTML
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.entity.TextReport
import java.text.NumberFormat

class KotlinxHtmlAuditListReport(
        runTimeNumberFormat: NumberFormat
) : AuditListReport(runTimeNumberFormat) {

    override fun render(event: RunEvent) = createHTML().html {
        head {
            title("${event.name} - ${event.date} - Audit List")
            style(type = StyleType.textCss) {
                unsafe { raw("""
                    table { border-collapse: collapse; }
                    th, td { border: 1px solid black; }
                    td.penalty span:first-child { font-weight: bold; }
                """.trimIndent()
                )}
            }
        }
        body {
            auditListTable(event)
        }
    }

    private fun BODY.auditListTable(event: RunEvent)  = table {
        thead { }
        caption { text("Audit List") }
        auditListTableHead()
        tbody {
            event.runsBySequence.forEach { run ->
                auditListTableRow(run)
            }
        }
    }

    private fun TABLE.auditListTableHead() = thead { tr {
        th { text("Sequence") }
        th { text("Numbers") }
        th { text("Time") }
        th { text("Penalty") }
        th { text("Driver") }
        th { text("Car") }
    } }

    private fun TBODY.auditListTableRow(run: Run) = tr {
        td("sequence") { text(run.sequence.toString()) }
        td("numbers") { text(run.registrationNumbers ?: "") }
        td("time") { text(run.rawTime?.let { runTimeNumberFormat.format(it) } ?: "" ) }
        auditListTablePenaltyData(run)
        td("driver") { text(run.registrationDriverName ?: "") }
        td("car") { text(run.registrationCarModel ?: "") }
    }

    private fun TR.auditListTablePenaltyData(run: Run) = td("penalty") {
        fun TD.penalty(penalty: String) {
            span { text(penalty) }
        }
        if (run.disqualified) penalty("DSQ")
        if (run.didNotFinish) penalty("DNF")
        if (run.rerun) penalty("RRN")
        if (run.cones > 0) penalty("+${run.cones}")
    }

}

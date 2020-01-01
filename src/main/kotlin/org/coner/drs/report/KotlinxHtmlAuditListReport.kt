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
        td("numbers") { text(run.registrationNumbers) }
        td("time") { text(runTimeNumberFormat.format(run.rawTime)) }
        auditListTablePenaltyData(run)
        td("driver") { text(run.registrationDriverName) }
        td("car") { text(run.registrationCarModel) }
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

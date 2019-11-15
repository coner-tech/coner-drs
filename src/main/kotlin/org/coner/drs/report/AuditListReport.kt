package org.coner.drs.report

import j2html.TagCreator.*
import j2html.tags.ContainerTag
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.RunEvent
import java.text.NumberFormat

class AuditListReport(
        private val event: RunEvent,
        private val runTimeNumberFormat: NumberFormat
) {
    fun render() = document(
            html(
                    head(
                            title("${event.name} - ${event.date} - Audit List"),
                            style("""
                                table { border-collapse: collapse; }
                                th, td { border: 1px solid black; }
                                td.penalty span:first-child { font-weight: bold; }
                            """.trimIndent()
                            ).withType("text/css")
                    ),
                    body(
                            renderAuditListTable()
                    )
            )
    )

    private fun renderAuditListTable() = table(
            caption("Audit List"),
            renderAuditListTableHead(),
            tbody(each(event.runsBySequence) { run: Run ->
                tr(*renderAuditListTableRow(run))
            })
    )

    private fun renderAuditListTableHead() = thead(
            th("Sequence"),
            th("Numbers"),
            th("Time"),
            th("Penalty"),
            th("Driver"),
            th("Car")
    )

    private fun renderAuditListTableRow(run: Run) = arrayOf(
            td(run.sequence.toString()),
            td(run.registrationNumbers),
            td(runTimeNumberFormat.format(run.rawTime)),
            renderAuditListTablePenaltyData(run),
            td(run.registrationDriverName),
            td(run.registrationCarModel)
    )

    private fun renderAuditListTablePenaltyData(run: Run): ContainerTag {
        val penalties = mutableListOf<String>().apply {
            if (run.disqualified) add("DSQ")
            if (run.didNotFinish) add("DNF")
            if (run.rerun) add("RRN")
            if (run.cones > 0) add("+${run.cones}")
        }
        val penaltySpans = each(penalties) { penalty ->
            span(penalty)
        }
        return td(penaltySpans).withClass("penalty")
    }

}
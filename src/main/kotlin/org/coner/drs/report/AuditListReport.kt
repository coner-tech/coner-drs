package org.coner.drs.report

import j2html.TagCreator.*
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.RunEvent

class AuditListReport {
    fun render(event: RunEvent) = document(
            html(
                    head(
                            title("${event.name} - ${event.date} - Audit List")
                    ),
                    body(
                            main()
                    )
            )
    )

    private fun main(event: RunEvent) = table(
            thead(
                    th("Sequence"),
                    th("Numbers"),
                    th("Penalty"),
                    th("Driver Name"),
                    th("Car Model")
            ),
            tbody(
                    each(ArrayList(event.runs), ::runRow)
            )
    )

    private fun runRow(run: Run) = tr(
            td(run.sequence.toString()),
            td(run.registrationNumbers),
            td(join(
                    iff(run.disqualified, "DSQ "),
                    iff(run.didNotFinish, "DNF "),
                    iff(run.cones > 0, run.cones.toString())
            )),
            td(run.registrationDriverName),
            td(run.registrationCarModel)
    )

}
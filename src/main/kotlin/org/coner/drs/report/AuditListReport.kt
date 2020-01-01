package org.coner.drs.report

import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.entity.TextReport
import java.text.NumberFormat

abstract class AuditListReport(
        protected val runTimeNumberFormat: NumberFormat
) {
    abstract fun render(event: RunEvent): String
}
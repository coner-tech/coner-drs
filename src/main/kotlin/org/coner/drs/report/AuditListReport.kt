package org.coner.drs.report

import org.coner.drs.domain.entity.RunEvent
import java.text.NumberFormat

abstract class AuditListReport(
        protected val event: RunEvent,
        protected val runTimeNumberFormat: NumberFormat
) {
    abstract fun render(): String?
}
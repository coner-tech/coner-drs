package org.coner.drs.domain.entity

sealed class TextReport(val content: String) {
    class AuditList(val event: Event, content: String) : TextReport(content)
}
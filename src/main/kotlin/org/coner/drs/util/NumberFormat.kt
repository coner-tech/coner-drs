package org.coner.drs.util

import java.text.NumberFormat

object NumberFormat {
    fun forRunTimes() = NumberFormat.getNumberInstance().apply {
        minimumIntegerDigits = 1
        minimumFractionDigits = 3
        maximumFractionDigits = 3
        isGroupingUsed = false
    }
}
package org.coner.drs.di

import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.compact.factory
import java.text.NumberFormat

object KatanaModules {
    val numberFormats = Module {
        factory(name = NumberFormats.RUN_TIME) {
            NumberFormat.getNumberInstance().apply {
                minimumIntegerDigits = 1
                minimumFractionDigits = 3
                maximumFractionDigits = 3
                isGroupingUsed = false
            }
        }
    }
}
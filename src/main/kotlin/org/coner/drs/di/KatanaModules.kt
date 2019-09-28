package org.coner.drs.di

import org.coner.drs.util.NumberFormat
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.compact.factory

fun numberFormatModule() = Module {
    factory(name = NumberFormatNames.RUN_TIME) { NumberFormat.forRunTimes() }
}
package org.coner.drs.di

import org.coner.drs.util.NumberFormat
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.compact.factory

val tornadofx.Component.katanaAppComponent: Component
    get() = (app as KatanaTrait).component

enum class NumberFormatNames {
    RUN_TIME
}

val numberFormatModule = Module {
    factory(name = NumberFormatNames.RUN_TIME) { NumberFormat.forRunTimes() }
}
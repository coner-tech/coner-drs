package org.coner.drs.di

import org.coner.drs.util.NumberFormat
import org.rewedigital.katana.Component
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.compact.factory

interface KatanaInjected {
    val component: Component
}

val tornadofx.Component.katanaApp: KatanaInjected
    get() = when (val app = app) {
        is KatanaInjected -> app
        else -> throw IllegalStateException("Can't access \"katanaApp\" because \"app\" isn't KatanaInjected!")
    }

enum class NumberFormatNames {
    RUN_TIME
}

fun numberFormatModule() = Module {
    factory(name = NumberFormatNames.RUN_TIME) { NumberFormat.forRunTimes() }
}
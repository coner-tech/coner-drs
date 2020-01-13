/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.coner.drs.di

import org.coner.drs.report.AuditListReport
import org.coner.drs.report.KotlinxHtmlAuditListReport
import org.coner.drs.util.NumberFormat
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.compact.factory
import org.rewedigital.katana.dsl.compact.singleton
import org.rewedigital.katana.dsl.get

val tornadofx.Component.katanaAppComponent: Component
    get() = (app as KatanaTrait).component

enum class NumberFormatNames {
    RUN_TIME
}

val numberFormatModule = Module {
    factory(name = NumberFormatNames.RUN_TIME) { NumberFormat.forRunTimes() }
}

val reportModule = Module() {
    singleton<AuditListReport> { KotlinxHtmlAuditListReport(get(NumberFormatNames.RUN_TIME)) }
}
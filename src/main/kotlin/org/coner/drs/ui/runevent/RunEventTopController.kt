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

package org.coner.drs.ui.runevent

import com.github.thomasnield.rxkotlinfx.actionEvents
import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javafx.application.Platform
import org.coner.drs.domain.entity.TextReport
import org.coner.drs.io.gateway.EventReportGateway
import tornadofx.*
import java.awt.Desktop

class RunEventTopController : Controller() {

    private val eventModel: RunEventModel by inject()
    private val disposables = CompositeDisposable()
    private val view: RunEventTopView by inject()
    private val reportGateway: EventReportGateway by inject()

    fun docked() {
        disposables += view.fileExit.actionEvents()
                .subscribe { Platform.exit() }
        disposables += view.reportsAuditList.actionEvents()
                .observeOn(Schedulers.io())
                .subscribe { displayReportAuditList() }
        disposables += view.helpAbout.actionEvents()
                .subscribe { TODO() }
    }

    fun undocked() {
        disposables.clear()
    }

    fun displayReportAuditList() {
        val report = reportGateway.pathTo(eventModel.event)
        Desktop.getDesktop().open(report.toFile())
    }
}
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
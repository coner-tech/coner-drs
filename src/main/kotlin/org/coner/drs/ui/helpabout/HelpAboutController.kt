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

package org.coner.drs.ui.helpabout

import com.github.thomasnield.rxkotlinfx.actionEvents
import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javafx.event.ActionEvent
import org.coner.drs.di.domainServicesModule
import org.coner.drs.di.katanaAppComponent
import org.coner.drs.domain.service.VersionService
import org.coner.drs.util.browseTo
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import tornadofx.*
import java.awt.Desktop
import java.net.URI
import javax.swing.SwingUtilities

class HelpAboutController : Controller(), KatanaTrait {

    override val component = Component(
            modules = listOf(
                    domainServicesModule
            )
    )

    private val model by inject<HelpAboutModel>()

    private val view by inject<HelpAboutView>()
    private val descriptionView by inject<DescriptionView>()

    private val disposables = CompositeDisposable()

    private val versionService: VersionService by component.inject()

    fun showViewAsModal() {
        view.openModal()
    }

    fun docked() {
        model.version = versionService.version
        disposables += view.closeButton.actionEvents()
                .observeOnFx()
                .subscribe { onHelpAboutCloseButtonClicked() }
        disposables += descriptionView.sourceCodeHyperlink.actionEvents()
                .subscribe { onHelpAboutDescriptionSourceCodeHyperlinkClicked() }
        requireNotNull(view.currentStage).let {
            it.minWidth = it.width
            it.minHeight = it.height
        }
    }

    fun undocked() {
        disposables.clear()
    }

    fun onHelpAboutCloseButtonClicked() {
        view.close()
    }

    private fun onHelpAboutDescriptionSourceCodeHyperlinkClicked() {
        Desktop.getDesktop().browseTo("http://github.com/caeos/coner-drs")
    }
}
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

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import org.coner.drs.ui.DrsStylesheet
import org.coner.drs.ui.icon.IconView
import org.coner.drs.ui.logo.LogoView
import org.coner.style.ConerFxStylesheet
import tornadofx.*

class HelpAboutView : View("About") {

    var closeButton: Button by singleAssign()

    private val model: HelpAboutModel by inject()

    override val root = vbox {
        vbox {
            addClass(DrsStylesheet.topBar)
            alignment = Pos.CENTER
            add<LogoView>(LogoView::size to LogoView.Size.X256)
            label("Digital Raw Sheets") {
                addClass(ConerFxStylesheet.h1)
            }
            hbox {
                alignment = Pos.CENTER
                label("Version ")
                label(model.versionProperty)
            }
        }
        tabpane {
            tab<DescriptionView>()
            tab<LicenseView>()
            tab<AuthorsView>()
            tab<AcknowledgementsView>()
            this.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
            vgrow = Priority.ALWAYS
        }
        buttonbar {
            button(text = "Close", type = ButtonBar.ButtonData.CANCEL_CLOSE) {
                closeButton = this
            }
            padding = insets(8)
        }
    }

    override fun onDock() {
        super.onDock()
        find<HelpAboutController>().docked()
    }

    override fun onUndock() {
        super.onUndock()
        find<HelpAboutController>().undocked()
    }
}
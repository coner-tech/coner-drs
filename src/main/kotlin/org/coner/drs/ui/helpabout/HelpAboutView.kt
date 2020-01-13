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
import javafx.scene.control.TabPane
import org.coner.drs.ui.DrsStylesheet
import org.coner.drs.ui.icon.IconView
import org.coner.style.ConerFxStylesheet
import tornadofx.*

class HelpAboutView : View("About") {

    override val root = vbox {
        hbox {
            addClass(DrsStylesheet.topBar)
            add<IconView>(IconView::size to IconView.Size.X128)
            vbox {
                alignment = Pos.BOTTOM_LEFT
                label("Coner") {
                    addClass(ConerFxStylesheet.h1)
                }
                label("Digital Raw Sheet") {
                    addClass(ConerFxStylesheet.h2)
                }
            }
        }
        tabpane {
            tab<DescriptionView>()
            tab<LicenseView>()
            tab<AuthorsView>()
            tab<AcknowledgementsView>()
            this.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        }
    }

    override fun onDock() {
        super.onDock()
        currentStage?.let {
            it.minWidth = 480.0
            it.minHeight = 320.0
        }
    }

}
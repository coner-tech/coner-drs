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
import javafx.scene.layout.Priority
import javafx.scene.text.TextAlignment
import org.coner.drs.ui.DrsStylesheet
import org.coner.drs.ui.logo.LogoView
import org.coner.style.ConerFxStylesheet
import tornadofx.*

class SplashView : View() {

    private val model: HelpAboutModel by inject()

    override val root = vbox {
        addClass(DrsStylesheet.topBar)
        alignment = Pos.CENTER
        add<LogoView>(LogoView::size to LogoView.Size.X256)
        label("Digital Raw Sheets") {
            addClass(ConerFxStylesheet.h1)
        }
        hbox {
            textflow {
                label("Version ")
                label(model.versionProperty)
            }
            pane {
                hgrow = Priority.ALWAYS
            }
            textflow {
                label("Copyright ")
                label(model.licenseYearProperty.stringBinding { it?.toString() })
                label(" Carlton Whitehead")
            }
        }
    }
}
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

import javafx.scene.control.Hyperlink
import tornadofx.*
import tornadofx.controlsfx.hyperlinklabel

class DescriptionView : View("Description") {

    var sourceCodeHyperlink: Hyperlink by singleAssign()

    private val description = """
        Coner is free, open source autocross event operations software.
        
        Digital Raw Sheets is an app replacement for paper raw sheets. It reduces the drag of working raw sheets by acquiring times automatically and making it push-button simple to key in drivers and penalties.
        
        
    """.trimIndent()

    override val root = vbox {
        padding = insets(8)
        textflow {
            text(description)
            hyperlink("Source Code") {
                sourceCodeHyperlink = this
            }
        }
    }
}
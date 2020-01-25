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
import javafx.scene.control.Hyperlink
import javafx.scene.layout.Priority
import javafx.scene.text.FontWeight
import javafx.scene.text.TextAlignment
import tornadofx.*

class CreditsView : View("Credits") {

    var fileAnIssueHyperlink: Hyperlink by singleAssign()

    private val model: CreditsModel by inject()

    override val root = scrollpane {
        isFitToWidth = true
        stackpane {
            padding = insets(8)
            textflow {
                model.credits.forEachIndexed { index, section ->
                    textAlignment = TextAlignment.CENTER
                    text(section.name) {
                        style {
                            fontWeight = FontWeight.BOLD
                        }
                    }
                    text("\n")
                    text(section.credits.joinToString("\n") { it.name })
                    text("\n\n")
                }
                text("If someone is missing from this list, please")
                hyperlink("file an issue.") {
                    fileAnIssueHyperlink = this
                }
            }
        }
    }
}
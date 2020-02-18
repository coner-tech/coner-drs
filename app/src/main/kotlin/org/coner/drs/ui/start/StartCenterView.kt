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

package org.coner.drs.ui.start

import javafx.scene.control.Button
import tornadofx.*

class StartCenterView : View() {

    private val model: StartModel by inject()

    var rawSheetDatabaseChooseButton: Button by singleAssign()
    var crispyFishDatabaseChooseButton: Button by singleAssign()
    var startButton: Button by singleAssign()

    override val root = stackpane {
        id = "start"
        form {
            fieldset("Databases") {
                field("Coner Digital Raw Sheet") {
                    textfield(model.rawSheetDatabaseProperty.stringBinding { it?.toString() ?: "" }) {
                        id = "raw-sheet-database-field"
                        isEditable = false
                    }
                    button("Choose") {
                        rawSheetDatabaseChooseButton = this
                    }
                }
                field("Crispy Fish") {
                    textfield(model.crispyFishDatabaseProperty.stringBinding { it?.absolutePath ?: ""}) {
                        id = "crispy-fish-database-field"
                        isEditable = false
                    }
                    button("Choose") {
                        crispyFishDatabaseChooseButton = this
                    }
                }
            }
            fieldset("Publish/Subscribe") {
                field("Subscriber") {
                    checkbox(property = model.subscriberProperty)
                }
            }
            buttonbar {
                button("Start") {
                    startButton = this
                    id = "start-button"
                    enableWhen { model.valid }
                    isDefaultButton = true
                }
            }
        }
    }
}
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

package org.coner.drs.ui.alterdriversequence

import javafx.geometry.Orientation
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
import org.coner.drs.ui.RegistrationCellFragment
import org.coner.drs.util.UpperCaseTextFormatter
import org.coner.drs.util.tornadofx.takeVerticalArrowKeyPressesAsSelectionsFrom
import tornadofx.*

class SpecifyDriverSequenceAlterationView : View() {

    private val model: SpecifyDriverSequenceAlterationModel by inject()
    private val controller: SpecifyDriverSequenceAlterationController = find()

    private lateinit var numbersTextField: TextField

    override val root = form {
        id = "specify-driver-sequence-alteration"
        prefWidth = 300.0
        fieldset(text = "Specify Sequence Alteration", labelPosition = Orientation.VERTICAL) {
            field("Sequence", orientation = Orientation.VERTICAL) {
                textfield(model.sequenceProperty) {
                    isEditable = false
                }
            }
            field("Relative", orientation = Orientation.VERTICAL) {
                togglegroup {
                    hbox {
                        togglebutton(
                                text = "Before",
                                value = InsertDriverIntoSequenceRequest.Relative.BEFORE,
                                group = this@togglegroup
                        ) {
                            id = "relative-before"
                        }
                        togglebutton(
                                text = "After",
                                value = InsertDriverIntoSequenceRequest.Relative.AFTER,
                                group = this@togglegroup
                        ) {
                            id = "relative-after"
                        }
                    }
                    bind(model.relativeProperty)
                }
            }
            field("_Numbers", orientation = Orientation.VERTICAL) {
                (inputContainer as VBox).spacing = 0.0
                textfield(model.numbersFieldProperty) {
                    id = "numbers"
                    numbersTextField = this
                    label.labelFor = this
                    label.isMnemonicParsing = true
                    required()
                    textFormatter = UpperCaseTextFormatter()
                }
                listview<Registration> {
                    id = "registrations-list-view"
                    vgrow = Priority.ALWAYS
                    model.registrationList.bindTo(this)
                    cellFragment(RegistrationCellFragment::class)
                    bindSelected(model.registrationProperty)
                    takeVerticalArrowKeyPressesAsSelectionsFrom(numbersTextField)
                    model.registrationListAutoSelectionCandidateProperty.onChange {
                        runLater {
                            selectionModel.select(it?.registration)
                            scrollTo(it?.registration)
                        }
                    }
                    subscribe<ResetEvent> {
                        selectionModel.clearSelection()
                        scrollTo(0)
                    }
                }
            }
        }
    }

    private val numbersField: TextField
        get() = root.lookup("#numbers") as TextField

    init {
        subscribe<ResetEvent> { reset() }
    }

    private fun reset() {
        runLater { numbersField.requestFocus() }
    }
}

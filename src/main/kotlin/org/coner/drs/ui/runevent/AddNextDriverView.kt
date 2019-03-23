package org.coner.drs.ui.runevent

import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCombination
import javafx.scene.layout.Priority
import org.coner.drs.domain.entity.Registration
import org.coner.drs.ui.RegistrationCellFragment
import org.coner.drs.ui.validation.NumbersFieldValidationController
import org.coner.drs.util.UpperCaseTextFormatter
import tornadofx.*

class AddNextDriverView : View("Add Next Driver") {
    private val model: AddNextDriverModel by inject()
    private val controller: AddNextDriverController by inject()
    private val numbersFieldValidation: NumbersFieldValidationController by inject()
    private val runEventModel: RunEventModel by inject()

    private var numbersField: TextField by singleAssign()
    private var registrationListView: ListView<Registration> by singleAssign()

    override val root = form {
        id = "add-next-driver"
        prefWidth = 300.0
        fieldset(text = title, labelPosition = Orientation.VERTICAL) {
            vgrow = Priority.ALWAYS
            field(text = "Sequence", orientation = Orientation.VERTICAL) {
                textfield(runEventModel.event.runForNextDriverProperty.select { it.sequenceProperty }) {
                    isEditable = false
                }
            }
            field(text = "_Numbers", orientation = Orientation.VERTICAL) {
                vgrow = Priority.ALWAYS
                textfield(model.numbersFieldProperty) {
                    numbersField = this
                    textFormatter = UpperCaseTextFormatter()
                    label.labelFor = this
                    label.isMnemonicParsing = true
                    setOnKeyPressed {
                        when (it.code) {
                            KeyCode.DOWN -> {
                                selectNextRegistration()
                                it.consume()
                            }
                            KeyCode.UP -> {
                                selectPreviousRegistration()
                                it.consume()
                            }
                            else -> { /* no-op */ }
                        }
                    }
                }
                listview<Registration> {
                    registrationListView = this
                    id = "registrations-list-view"
                    vgrow = Priority.ALWAYS
                    model.registrationList.bindTo(this)
                    cellFragment(RegistrationCellFragment::class)
                    bindSelected(model.registrationListSelectionProperty)
                    model.registrationListAutoSelectionCandidateProperty.onChange {
                        runLater {
                            selectionModel.select(it?.registration)
                            scrollTo(it?.registration)
                        }
                    }
                    shortcut("Enter") {
                        if (!this.isFocused) return@shortcut
                        if (this.selectedItem == null) return@shortcut
                        addFromListSelectionAndReset()
                    }

                }
            }
            hbox {
                alignment = Pos.TOP_RIGHT
                splitmenubutton("Add") {
                    item(name = "Force Exact Numbers", keyCombination = KeyCombination.keyCombination("Ctrl+Enter")) {
                        enableWhen(model.numbersFieldContainsNumbersTokensBinding)
                        action { addFromExactNumbersAndReset() }
                    }
                    enableWhen(
                            model.numbersFieldContainsNumbersTokensBinding
                                    .or(model.registrationListSelectionProperty.isNotNull)
                    )
                    action { addFromListSelectionAndReset() }
                    shortcut(KeyCombination.keyCombination("Enter")) {
                        if (model.registrationListSelectionProperty.isNull.get()) return@shortcut
                        addFromListSelectionAndReset()
                    }
                }
            }
        }
    }

    private fun selectNextRegistration() {
        registrationListView.selectionModel.selectNext()
        runLater { registrationListView.scrollTo(registrationListView.selectedItem) }
    }

    private fun selectPreviousRegistration() {
        registrationListView.selectionModel.selectPrevious()
        runLater { registrationListView.scrollTo(registrationListView.selectedItem) }
    }

    private fun addFromListSelectionAndReset() {
        controller.addNextDriverFromRegistrationListSelection()
        reset()
    }

    private fun addFromExactNumbersAndReset() {
        controller.addNextDriverForceExactNumbers()
        reset()
    }

    private fun reset() {
        model.numbersField = ""
        numbersField.requestFocus()
    }

    init {
        controller.init()
    }
}
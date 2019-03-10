package org.coner.drs.ui.runevent

import javafx.geometry.Orientation
import javafx.scene.Group
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.util.StringConverter
import org.coner.drs.ui.validation.NumbersFieldValidationController
import org.coner.drs.util.UpperCaseTextFormatter
import org.coner.drs.util.bindAutoCompletion
import tornadofx.*

class AddNextDriverView : View("Add Next Driver") {
    private val model: AddNextDriverModel by inject()
    private val controller: AddNextDriverController by inject()
    private val numbersFieldValidation: NumbersFieldValidationController by inject()

    override val root = form {
        minWidth = 280.0
        prefWidth = minWidth
        fieldset(text = title, labelPosition = Orientation.VERTICAL) {
            field(text = "Sequence") {
                textfield(model.nextRunSequenceProperty) {
                    isEditable = false
                    prefColumnCount = 4
                }
            }
            field(text = "Numbers") {
                vbox(spacing = 10) {
//                    prefHeightProperty().bind(this@field.heightProperty())
                    textfield(model.numbersFieldProperty) {
                        required()
                        validator(
                                trigger = ValidationTrigger.OnChange(),
                                validator = numbersFieldValidation.validator
                        )
//                    bindAutoCompletion(suggestionsProvider = { controller.buildNumbersFieldSuggestions() }) {
//                        setDelay(0)
//                    }
                        prefColumnCount = 8
                        textFormatter = UpperCaseTextFormatter()
                    }
                    listview(controller.model.registrationsForNumbersField) {
                        vgrow = Priority.ALWAYS
                        cellFragment(RegistrationCellFragment::class)
                    }
                }
            }
            buttonbar {
                button("Add") {
                    enableWhen { model.valid }
                    action { controller.addNextDriver() }
                    tooltip("Shortcut: Enter")
                    isDefaultButton = true
                }
            }
        }
    }

    fun onNewRun(toFocus: TextField) {
        toFocus.requestFocus()
    }
}
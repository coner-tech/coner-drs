package org.coner.drs.ui.runevent

import javafx.geometry.Orientation
import javafx.scene.control.TextField
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
        fieldset(text = title, labelPosition = Orientation.VERTICAL) {
            hgrow = Priority.NEVER
            field(text = "Sequence") {
                textfield(model.nextRunSequenceProperty) {
                    isEditable = false
                    prefColumnCount = 4
                }
            }
            field(text = "Numbers") {
                textfield(model.numbersFieldProperty) {
                    required()
                    validator(
                            trigger = ValidationTrigger.OnChange(),
                            validator = numbersFieldValidation.validator
                    )
                    bindAutoCompletion(suggestionsProvider = { controller.buildNumbersFieldSuggestions() }) {
                        setDelay(0)
                    }
                    prefColumnCount = 5
                    textFormatter = UpperCaseTextFormatter()
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
        fieldset("Registration", labelPosition = Orientation.VERTICAL) {
            field("Name") {
                textfield(model.registrationForNumbersFieldProperty.select { it.nameProperty }) {
                    isEditable = false
                }
            }
            field("Car Model") {
                textfield(model.registrationForNumbersFieldProperty.select { it.carModelProperty }) {
                    isEditable = false
                }
            }
            field("Car Color") {
                textfield(model.registrationForNumbersFieldProperty.select { it.carColorProperty }) {
                    isEditable = false
                }
            }
        }
    }

    fun onNewRun(toFocus: TextField) {
        toFocus.requestFocus()
    }
}
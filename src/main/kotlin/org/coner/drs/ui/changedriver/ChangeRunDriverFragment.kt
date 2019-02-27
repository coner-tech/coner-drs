package org.coner.drs.ui.changedriver

import javafx.geometry.Orientation
import javafx.scene.control.ButtonBar
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.ui.validation.NumbersFieldValidationController
import org.coner.drs.util.UpperCaseTextFormatter
import org.coner.drs.util.bindAutoCompletion
import tornadofx.*

class ChangeRunDriverFragment : Fragment("Change Run Driver") {

    override val scope = super.scope as ChangeRunDriverScope

    val model: ChangeRunDriverModel by inject()
    val controller: ChangeRunDriverController by inject()
    val numbersFieldValidation: NumbersFieldValidationController by inject()

    override val root = form {
        fieldset(text = title, labelPosition = Orientation.VERTICAL) {
            field("Sequence") {
                textfield(model.run.sequenceProperty) {
                    isEditable = false
                }
            }
            field("Numbers") {
                textfield(model.numbersProperty) {
                    required()
                    validator(
                            trigger = ValidationTrigger.OnChange(),
                            validator = numbersFieldValidation.validator
                    )
                    bindAutoCompletion(suggestionsProvider = { controller.buildNumbersHints() }) {
                        setDelay(0)
                    }
                    textFormatter = UpperCaseTextFormatter()
                    runLater { requestFocus() }
                }
            }
            buttonbar {
                button("Cancel", type = ButtonBar.ButtonData.CANCEL_CLOSE) {
                    action {
                        close()
                    }
                }
                button("Change", type = ButtonBar.ButtonData.OK_DONE) {
                    enableWhen { model.valid }
                    action {
                        controller.changeDriver()
                        close()
                    }
                    isDefaultButton = true
                }
            }
        }
        fieldset("Registration", labelPosition = Orientation.VERTICAL) {
            field("Name") {
                textfield(model.registrationNameProperty) {
                    isEditable = false
                }
            }
            field("Car Model") {
                textfield(model.registrationCarModelProperty) {
                    isEditable = false
                }
            }
            field("Car Color") {
                textfield(model.registrationCarColorProperty) {
                    isEditable = false
                }
            }
        }
    }
}
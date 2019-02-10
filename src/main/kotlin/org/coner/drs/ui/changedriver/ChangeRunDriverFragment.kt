package org.coner.drs.ui.changedriver

import javafx.geometry.Orientation
import javafx.scene.control.ButtonBar
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.Run
import org.coner.drs.util.UpperCaseTextFormatter
import org.coner.drs.util.bindAutoCompletion
import tornadofx.*

class ChangeRunDriverFragment : Fragment("Change Run Driver") {

    override val scope = super.scope as ChangeRunDriverScope

    val model: ChangeRunDriverModel by inject()
    val controller: ChangeRunDriverController by inject()

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
        fieldset(text = "Identity") {
            field("Name") {
                textfield(model.registrationForNumbersProperty.select(Registration::nameProperty)) {
                    isEditable = false
                }
            }
        }
        fieldset("Car") {
            field("Model") {
                textfield(model.registrationForNumbersProperty.select(Registration::carModelProperty)) {
                    isEditable = false
                }
            }
            field("Color") {
                textfield(model.registrationForNumbersProperty.select(Registration::carColorProperty)) {
                    isEditable = false
                }
            }
        }
    }
}
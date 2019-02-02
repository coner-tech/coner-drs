package org.coner.drs.ui.changedriver

import javafx.geometry.Orientation
import org.coner.drs.domain.entity.Run
import org.coner.drs.util.bindAutoCompletion
import tornadofx.*

class ChangeRunDriverFragment : Fragment("Change Run Driver") {

    override val scope = super.scope as ChangeRunDriverScope

    val model: ChangeRunDriverModel by inject()
    val controller: ChangeRunDriverController by inject()

    override val root = form {
        fieldset(text = title, labelPosition = Orientation.VERTICAL) {
            field("Sequence") {
                textfield(scope.run.sequenceProperty) {
                    isEditable = false
                }
            }
            field("Numbers") {
                textfield(model.numbersProperty) {
                    required()
                    bindAutoCompletion(suggestionsProvider = { controller.buildNumbersHints() }) {
                        setDelay(0)
                    }
                    runLater { requestFocus() }
                }
            }
            buttonbar {
                button("Change") {
                    enableWhen { model.valid }
                    action { controller.changeDriver() }
                    isDefaultButton = true
                }
            }
        }
    }
}
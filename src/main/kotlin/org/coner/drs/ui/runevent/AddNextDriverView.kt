package org.coner.drs.ui.runevent

import javafx.geometry.Orientation
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.util.StringConverter
import org.coner.drs.util.UpperCaseTextFormatter
import org.coner.drs.util.bindAutoCompletion
import tornadofx.*

class AddNextDriverView : View("Add Next Driver") {
    private val model: AddNextDriverModel by inject()
    private val controller: AddNextDriverController by inject()

    override val root = form {
        fieldset(text = title, labelPosition = Orientation.VERTICAL) {
            hgrow = Priority.NEVER
            field(text = "Sequence") {
                textfield(model.nextDriver.sequence) {
                    isEditable = false
                    prefColumnCount = 4
                }
            }
            field(text = "Numbers") {
                textfield(model.numbersFieldProperty) {
                    required()
                    bindAutoCompletion(suggestionsProvider = { controller.buildNumbersHints() }) {
                        setDelay(0)
                    }
                    prefColumnCount = 5
                    model.nextDriver.itemProperty.onChange {
                        onNewRun(this)
                    }
                    textFormatter = UpperCaseTextFormatter()
                }
            }
            buttonbar {
                button("Add") {
                    enableWhen { model.nextDriver.valid }
                    action { controller.addNextDriver() }
                    tooltip("Shortcut: Enter")
                    isDefaultButton = true
                }
            }
        }
        fieldset("Identity", labelPosition = Orientation.VERTICAL) {
            field("Name") {
                textfield(/* TODO */) {
                    isEditable = false
                }
            }
        }
        fieldset("Car", labelPosition = Orientation.VERTICAL) {
            field("Model") {
                textfield(/* TODO */) {
                    isEditable = false
                }
            }
            field("Color") {
                textfield(/* TODO */) {
                    isEditable = false
                }
            }
        }
    }

    fun onNewRun(toFocus: TextField) {
        model.nextDriver.validate(decorateErrors = false)
        toFocus.requestFocus()
    }

    override fun onDock() {
        super.onDock()
        controller.buildNextDriver()
    }
}
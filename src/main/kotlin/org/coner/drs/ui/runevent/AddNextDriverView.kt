package org.coner.drs.ui.runevent

import javafx.geometry.Orientation
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.util.StringConverter
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
                    promptTextProperty().bind(model.driverAutoCompleteOrderPreferenceProperty.stringBinding { it?.text })
                }
            }
            buttonbar {
                button("Add") {
                    enableWhen { model.nextDriver.valid }
                    action { controller.addNextDriver() }
                    tooltip("Shortcut: Ctrl+Enter")
                    isDefaultButton = true
                }
            }
            shortcut("Ctrl+Enter") {
                if (model.nextDriver.isValid) {
                    controller.addNextDriver()
                }
            }
        }
        fieldset("Identity", labelPosition = Orientation.VERTICAL) {
            field("Name") {
                textfield(model.registrationForNumbersProperty.select { it.nameProperty }) {
                    isEditable = false
                }
            }
        }
        fieldset("Car", labelPosition = Orientation.VERTICAL) {
            field("Model") {
                textfield(model.registrationForNumbersProperty.select { it.carModelProperty }) {
                    isEditable = false
                }
            }
            field("Color") {
                textfield(model.registrationForNumbersProperty.select { it.carColorProperty }) {
                    isEditable = false
                }
            }
        }
        pane {
            vgrow = Priority.ALWAYS
        }
        fieldset(text = "Preferences" ,labelPosition = Orientation.VERTICAL) {
            field("Driver Auto-Complete Order") {
                choicebox(
                        property = model.driverAutoCompleteOrderPreferenceProperty,
                        values = model.driverAutoCompleteOrderPreferences
                ) {
                    converter = DriverAutoCompleteOrderPreferenceStringConverter()
                }
            }
        }
    }

    private inner class DriverAutoCompleteOrderPreferenceStringConverter(
    ) : StringConverter<DriverAutoCompleteOrderPreference>() {
        override fun toString(p0: DriverAutoCompleteOrderPreference) = p0.text

        override fun fromString(p0: String) = model.driverAutoCompleteOrderPreferences.first { it.text == p0 }

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
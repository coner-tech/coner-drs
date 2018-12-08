package org.coner.drs.ui.run_event

import javafx.scene.control.ButtonBar
import org.coner.drs.TimerConfiguration
import org.coner.drs.io.timer.TimerService
import tornadofx.*
import java.io.File
import java.io.FileNotFoundException

class TimerConfigurationView : View("Configure Timer") {

    private val model: TimerConfigurationModel by inject()
    private val runEventModel: RunEventModel by inject()
    private val timerService: TimerService by inject()

    override val root = form {
        minWidth = 640.0
        minHeight = 480.0
        fieldset(title) {
            field("Type") {
                choicebox(property = model.typeProperty, values = model.types)
            }
            field("Port") {
                combobox(property = model.serialPortProperty, values = model.serialPorts).required()
                button("Refresh") { action { refreshSerialPorts() }  }
                managedWhen { model.typeProperty.isEqualTo(TimerConfiguration.SerialPortInput.label) }
                visibleWhen(managedProperty())
            }
            field("File") {
                textfield(model.inputFileProperty) {
                    required()
                }
                button("Choose") { action { chooseInputFile() } }
                managedWhen { model.typeProperty.isEqualTo(TimerConfiguration.FileInput.label) }
                visibleWhen(managedProperty())
            }
        }
        buttonbar {
            button("Clear", ButtonBar.ButtonData.OTHER) {
                action { clearConfiguration() }
            }
            button("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE) {
                action { close() }
            }
            button("Apply", ButtonBar.ButtonData.OK_DONE) {
                enableWhen {
                    booleanBinding(model.typeProperty, model.serialPortProperty, model.inputFileProperty) {
                        when (model.type) {
                            TimerConfiguration.SerialPortInput.label -> model.serialPort?.isNotBlank()
                                    ?: false
                            TimerConfiguration.FileInput.label -> model.inputFile?.isNotBlank() ?: false
                            else -> throw IllegalArgumentException("Unrecognized type: ${model.type}")
                        }
                    }
                }
                action { save() }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        refreshSerialPorts()
    }

    override fun onUndock() {
        super.onUndock()
        model.rollback()
    }

    private fun refreshSerialPorts() {
        runAsync {
            timerService.listSerialPorts()
        } ui {
            model.serialPorts.clear()
            model.serialPorts.addAll(it)
        }
    }

    private fun chooseInputFile() {
        val choice = chooseFile(
                title = "Choose Timer Input File",
                mode = FileChooserMode.Single,
                owner = this@TimerConfigurationView.currentWindow,
                filters = emptyArray()
        ).firstOrNull()
        model.inputFile = choice?.absolutePath
    }

    private fun save() {
        runEventModel.timerConfiguration = when (model.type) {
            TimerConfiguration.SerialPortInput.label -> {
                TimerConfiguration.SerialPortInput(model.serialPort)
            }
            TimerConfiguration.FileInput.label -> {
                val file = File(model.inputFile)
                if (!file.exists() || !file.isFile) throw FileNotFoundException("File not found: ${model.inputFile}")
                TimerConfiguration.FileInput(file)
            }
            else -> throw IllegalArgumentException("Unrecognized type: ${model.type}")
        }
        close()
    }

    private fun clearConfiguration() {
        runEventModel.timerConfiguration = null
        close()
    }
}
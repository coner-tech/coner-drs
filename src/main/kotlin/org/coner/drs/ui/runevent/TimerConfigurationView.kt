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

package org.coner.drs.ui.runevent

import javafx.scene.control.ButtonBar
import org.coner.drs.domain.entity.TimerConfiguration
import org.coner.drs.io.timer.TimerService
import org.coner.drs.ui.TimerConfigurationConverter
import tornadofx.*
import java.io.File
import java.io.FileNotFoundException

class TimerConfigurationView : View("Configure Timer") {

    private val model: TimerConfigurationModel by inject()
    private val runEventModel: RunEventModel by inject()
    private val timerService: TimerService by inject()

    override val root = form {
        id = "timer-configuration"
        minWidth = 640.0
        minHeight = 480.0
        fieldset(title) {
            field("Type") {
                choicebox(property = model.typeProperty, values = model.types) {
                    id = "type"
                    converter = TimerConfigurationConverter()
                }
            }
            field("Port") {
                combobox(property = model.serialPortProperty, values = model.serialPorts).required()
                button("Refresh") { action { refreshSerialPorts() }  }
                managedWhen { model.typeProperty.isEqualTo(TimerConfiguration.SerialPortInput::class) }
                visibleWhen(managedProperty())
            }
            field("File") {
                textfield(model.inputFileProperty) {
                    id = "file-textfield"
                    required()
                }
                button("Choose") { action { chooseInputFile() } }
                managedWhen { model.typeProperty.isEqualTo(TimerConfiguration.FileInput::class) }
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
                id = "apply"
                enableWhen {
                    booleanBinding(model.typeProperty, model.serialPortProperty, model.inputFileProperty) {
                        when (model.type) {
                            TimerConfiguration.SerialPortInput::class -> model.serialPort?.isNotBlank()
                                    ?: false
                            TimerConfiguration.FileInput::class -> model.inputFile?.isNotBlank() ?: false
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
            TimerConfiguration.SerialPortInput::class -> {
                TimerConfiguration.SerialPortInput(model.serialPort)
            }
            TimerConfiguration.FileInput::class -> {
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
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

import javafx.geometry.Orientation
import javafx.stage.Modality
import org.coner.drs.domain.entity.TimerConfiguration
import org.coner.drs.io.timer.TimerService
import tornadofx.*

class TimerView : View("Timer") {
    val model: RunEventModel by inject()
    val controller: RunEventController by inject()
    val timerService: TimerService by inject()
    override val root = form {
        id = "timer"
        fieldset(labelPosition = Orientation.VERTICAL) {
            field("Operation") {
                button(model.controlTextProperty) {
                    id = "operation-button"
                    enableWhen { model.timerConfigurationProperty.isNotNull }
                    action { runAsyncWithProgress { controller.toggleTimer() } }
                }
            }
            field(text = "Configuration") {
                vbox(spacing = 8) {
                    button("Configure") {
                        id = "configure-button"
                        enableWhen { timerService.model.timerProperty.isNull }
                        action { showConfiguration() }
                    }
                    text(model.timerConfigurationTextProperty)
                }
            }
            field(text = "Next Time Run Sequence") {
                textfield(model.event.runForNextTimeProperty.select { it.sequenceProperty }) {
                    isEditable = false
                }
            }
        }
    }

    private fun showConfiguration() {
        find<TimerConfigurationView>().openModal(
                modality = Modality.APPLICATION_MODAL,
                owner = currentWindow,
                block = true,
                escapeClosesWindow = true
        )
    }

    override fun onDock() {
        super.onDock()
        model.controlTextProperty.bind(timerService.model.timerProperty.stringBinding {
            if (it == null) "Start" else "Stop"
        })
        model.timerConfigurationTextProperty.bind(model.timerConfigurationProperty.stringBinding {
            when (it) {
                null -> "Not configured"
                is TimerConfiguration.FileInput -> "File: ${it.file.name}"
                is TimerConfiguration.SerialPortInput -> "Serial port: ${it.port}"
            }
        })
    }

    override fun onUndock() {
        super.onUndock()
        model.controlTextProperty.unbind()
        model.timerConfigurationTextProperty.unbind()
    }
}
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
        fieldset(labelPosition = Orientation.VERTICAL) {
            field("Operation") {
                button(model.controlTextProperty) {
                    enableWhen { model.timerConfigurationProperty.isNotNull }
                    action { runAsyncWithProgress { controller.toggleTimer() } }
                }
            }
            field(text = "Configuration") {
                vbox(spacing = 8) {
                    button("Configure") {
                        enableWhen { timerService.model.timerProperty.isNull }
                        action { showConfiguration() }
                    }
                    text(model.timerConfigurationTextProperty)
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
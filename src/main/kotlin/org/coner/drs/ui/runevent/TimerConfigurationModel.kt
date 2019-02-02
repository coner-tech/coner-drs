package org.coner.drs.ui.runevent

import javafx.beans.property.SimpleStringProperty
import org.coner.drs.domain.entity.TimerConfiguration
import tornadofx.*

class TimerConfigurationModel : ViewModel() {
    val types = observableList(
            TimerConfiguration.SerialPortInput.label,
            TimerConfiguration.FileInput.label
    )

    val typeProperty = SimpleStringProperty(this, "type", TimerConfiguration.SerialPortInput.label)
    var type by typeProperty

    val serialPortProperty = SimpleStringProperty(this, "serialPort")
    var serialPort by serialPortProperty

    val inputFileProperty = SimpleStringProperty(this, "inputFile")
    var inputFile by inputFileProperty


    val serialPorts = observableList<String>()

}

val TimerConfiguration.SerialPortInput.Companion.label: String get() = "Serial Port"
val TimerConfiguration.FileInput.Companion.label: String get() = "File"


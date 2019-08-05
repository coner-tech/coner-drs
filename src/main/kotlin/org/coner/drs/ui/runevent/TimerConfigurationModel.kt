package org.coner.drs.ui.runevent

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.coner.drs.domain.entity.TimerConfiguration
import tornadofx.*
import kotlin.reflect.KClass

class TimerConfigurationModel : ViewModel() {
    val types = observableListOf(
            TimerConfiguration.SerialPortInput::class,
            TimerConfiguration.FileInput::class
    )

    val typeProperty = SimpleObjectProperty<KClass<*>>(this, "type", TimerConfiguration.SerialPortInput::class)
    var type by typeProperty

    val serialPortProperty = SimpleStringProperty(this, "serialPort")
    var serialPort by serialPortProperty

    val inputFileProperty = SimpleStringProperty(this, "inputFile")
    var inputFile by inputFileProperty

    val serialPorts = observableListOf<String>()

}

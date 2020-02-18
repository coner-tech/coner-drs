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

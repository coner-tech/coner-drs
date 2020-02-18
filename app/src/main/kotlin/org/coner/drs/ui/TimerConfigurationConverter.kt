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

package org.coner.drs.ui

import javafx.util.StringConverter
import org.coner.drs.domain.entity.TimerConfiguration
import kotlin.reflect.KClass

class TimerConfigurationConverter : StringConverter<KClass<*>>() {
    override fun toString(p0: KClass<*>?): String {
        return when (p0) {
            TimerConfiguration.SerialPortInput::class -> "Serial"
            TimerConfiguration.FileInput::class -> "File"
            else -> throw IllegalArgumentException("Unknown TimerConfiguration: $p0")
        }
    }

    override fun fromString(p0: String?): KClass<*> {
        return when (p0) {
            "Serial" -> TimerConfiguration.SerialPortInput::class
            "File" -> TimerConfiguration.FileInput::class
            else -> throw IllegalArgumentException("Unknown string $p0")
        }
    }
}
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
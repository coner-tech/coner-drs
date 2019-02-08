package org.coner.drs.domain.entity

import java.io.File

sealed class TimerConfiguration {
    data class SerialPortInput(val port: String) : TimerConfiguration() { companion object { }}
    data class FileInput(val file: File) : TimerConfiguration() { companion object { }}
}
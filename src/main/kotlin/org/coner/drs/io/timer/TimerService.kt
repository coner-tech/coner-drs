package org.coner.drs.io.timer

import javafx.beans.property.SimpleObjectProperty
import org.coner.timer.Timer
import org.coner.timer.input.mapper.JACTimerInputMapper
import org.coner.timer.input.reader.InputStreamTimerInputReader
import org.coner.timer.input.reader.PureJavaCommTimerInputReader
import org.coner.timer.input.reader.TimerInputReaderController
import org.coner.timer.model.FinishTriggerElapsedTimeOnly
import org.coner.timer.output.TimerOutputWriter
import org.coner.timer.util.PureJavaCommWrapper
import purejavacomm.CommPortIdentifier
import tornadofx.*
import java.io.File
import tornadofx.getValue
import tornadofx.setValue

class TimerService : Controller() {

    val model: TimerModel by inject()

    fun startFileInputTimer(inputFile: File, timerOutputWriter: TimerOutputWriter<FinishTriggerElapsedTimeOnly>) {
        if (model.timer != null) throw IllegalStateException("Already started!")
        val reader = InputStreamTimerInputReader(inputFile.inputStream())
        val controller = TimerInputReaderController(reader = reader)
        val mapper = JACTimerInputMapper()
        val timer = Timer(controller, null, mapper, timerOutputWriter)
        timer.start()
        runLater {
            model.timer = timer
        }
    }

    fun startCommPortTimer(port: String, timerOutputWriter: TimerOutputWriter<FinishTriggerElapsedTimeOnly>) {
        if (model.timer != null) throw IllegalStateException("Already started!")
        val reader = PureJavaCommTimerInputReader(
                pureJavaComm = PureJavaCommWrapper(),
                appName = "Coner Digital Raw Sheets",
                port = port
        )
        val readerController = TimerInputReaderController(reader = reader)
        val mapper = JACTimerInputMapper()
        val timer = Timer(
                controller = readerController,
                mapper = mapper,
                mappedInputWriter = timerOutputWriter
        )
        timer.start()
        runLater {
            model.timer = timer
        }
    }

    fun stop() {
        model.timer?.stop()
        runLater {
            model.timer = null
        }
    }

    fun listSerialPorts(): List<String> {
        return CommPortIdentifier.getPortIdentifiers().toList()
                .map { it.name }
                .sorted()
    }
}

class TimerModel : ViewModel() {
    val timerProperty = SimpleObjectProperty<Timer<*, *>>(this, "timer", null)
    var timer by timerProperty
}
package org.coner.drs.ui.runevent

import com.github.thomasnield.rxkotlinfx.observeOnFx
import org.coner.drs.Run
import org.coner.drs.TimerConfiguration
import org.coner.drs.io.db.entityWatchEventConsumer
import org.coner.drs.io.service.RegistrationService
import org.coner.drs.io.service.RunService
import org.coner.drs.io.timer.TimerService
import org.coner.timer.model.FinishTriggerElapsedTimeOnly
import org.coner.timer.output.TimerOutputWriter
import tornadofx.*

class RunEventController : Controller() {
    val model: RunEventModel by inject()
    val registrationService: RegistrationService by inject()
    val runService: RunService by inject()
    val timerService: TimerService by inject()

    fun init() {
        runService.io.createDrsDbRunsPath(model.event)
        loadRuns()
        loadRegistrations()
    }

    fun loadRuns() {
        runAsync {
            runService.list(model.event)
        } success {
            model.runs.clear()
            model.runs.addAll(it)
        }
    }

    fun loadRegistrations() {
        runAsync {
            registrationService.list(model.event)
        } ui {
            model.registrations.clear()
            model.registrations.addAll(it)
        }
    }

    fun save(run: Run) {
        runAsync { runService.save(run) }
    }

    fun incrementCones(run: Run) {
        run.cones++
        runAsync { save(run) }
    }

    fun decrementCones(run: Run) {
        run.cones--
        save(run)
    }

    fun docked() {
        model.disposables.add(runService.watchList(model.event)
                .observeOnFx()
                .subscribe(entityWatchEventConsumer(
                        idProperty = Run::id,
                        list = model.runs
                ))
        )
    }

    fun undocked() {
        model.disposables.clear()
        timerService.stop()
    }

    fun toggleTimer() {
        if (timerService.model.timer == null) {
            val config = model.timerConfiguration
            when (config) {
                is TimerConfiguration.SerialPortInput -> {
                    timerService.startCommPortTimer(config.port, timerOutputWriter)
                }
                is TimerConfiguration.FileInput -> {
                    timerService.startFileInputTimer(config.file, timerOutputWriter)
                }
            }
        } else {
            timerService.stop()
        }
    }

    private val timerOutputWriter = object : TimerOutputWriter<FinishTriggerElapsedTimeOnly> {
        override fun write(input: FinishTriggerElapsedTimeOnly) {
            runService.addTimeToFirstRunInSequenceWithoutRawTime(model.event, input.et)
        }
    }
}
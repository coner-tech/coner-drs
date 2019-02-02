package org.coner.drs.ui.runevent

import com.github.thomasnield.rxkotlinfx.changes
import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.coner.drs.Registration
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
        Single.zip(
                registrationService.list(model.event),
                runService.list(model.event),
                BiFunction { registrations: List<Registration>, runs: List<Run> ->
                    registrations to runs
                }
        ).subscribeOn(Schedulers.io())
                .observeOnFx()
                .subscribe { (registrations, runs) ->
                    model.registrations.setAll(registrations)
                    model.runs.setAll(runs)
                    runService.hydrateWithRegistrationMetadata(runs, registrations)
                }
    }

    fun save(run: Run) {
        runAsync {
            runService.save(run)
        }
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
        model.disposables.addAll(
                runService.watchList(model.event, model.registrations)
                    .subscribeOn(Schedulers.io())
                    .observeOnFx()
                    .subscribe(entityWatchEventConsumer(
                            idProperty = Run::id,
                            list = model.runs
                    )),
                registrationService.watchList(model.event)
                        .subscribeOn(Schedulers.io())
                        .observeOnFx()
                        .subscribe {
                            model.registrations.setAll(it)
                            runService.hydrateWithRegistrationMetadata(model.runs, it, true)
                        }
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
package org.coner.drs.ui.runevent

import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.TimerConfiguration
import org.coner.drs.io.db.entityWatchEventConsumer
import org.coner.drs.io.service.RegistrationIoService
import org.coner.drs.io.service.RunIoService
import org.coner.drs.io.timer.TimerService
import org.coner.timer.model.FinishTriggerElapsedTimeOnly
import org.coner.timer.output.TimerOutputWriter
import tornadofx.*

class RunEventController : Controller() {
    val model: RunEventModel by inject()
    val registrationIoService: RegistrationIoService by inject()
    val runIoService: RunIoService by inject()
    val timerService: TimerService by inject()

    fun init() {
        Single.zip(
                registrationIoService.list(model.event),
                runIoService.list(model.event),
                BiFunction { registrations: List<Registration>, runs: List<Run> ->
                    registrations to runs
                }
        ).subscribeOn(Schedulers.io())
                .observeOnFx()
                .subscribe { (registrations, runs) ->
                    model.registrations.setAll(registrations)
                    model.runs.setAll(runs)
                    runIoService.hydrateWithRegistrationMetadata(runs, registrations)
                }
    }

    fun save(run: Run) {
        runAsync {
            runIoService.save(run)
        }
    }

    fun docked() {
        model.disposables.addAll(
                runIoService.watchList(model.event, model.registrations)
                    .subscribeOn(Schedulers.io())
                    .observeOnFx()
                    .subscribe(entityWatchEventConsumer(
                            idProperty = Run::id,
                            list = model.runs
                    )),
                registrationIoService.watchList(model.event)
                        .subscribeOn(Schedulers.io())
                        .observeOnFx()
                        .subscribe {
                            model.registrations.setAll(it)
                            runIoService.hydrateWithRegistrationMetadata(model.runs, it, true)
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
            runIoService.addTimeToFirstRunInSequenceWithoutRawTime(model.event, input.et)
        }
    }
}
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

import com.github.thomasnield.rxkotlinfx.doOnNextFx
import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import org.coner.drs.di.KatanaScopes
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.TimerConfiguration
import org.coner.drs.domain.service.ReportService
import org.coner.drs.domain.service.RunService
import org.coner.drs.io.db.entityWatchEventConsumer
import org.coner.drs.io.gateway.RegistrationGateway
import org.coner.drs.io.gateway.RunGateway
import org.coner.drs.io.timer.TimerService
import org.coner.drs.ui.start.StartModel
import org.coner.timer.model.FinishTriggerElapsedTimeOnly
import org.coner.timer.output.TimerOutputWriter
import org.rewedigital.katana.KatanaTrait
import tornadofx.*
import java.util.*
import java.util.concurrent.TimeUnit

class RunEventController : Controller(), KatanaTrait {
    val model: RunEventModel by inject()
    val registrationGateway: RegistrationGateway by inject()
    val runGateway: RunGateway by inject()
    val runService: RunService by inject()
    val timerService: TimerService by inject()
    val reportService: ReportService by inject()

    fun init(eventId: UUID, subscriber: Boolean) {
        TODO("global katana scope for RunEvent")
        model.event = eventService
        Single.zip(
                registrationGateway.list(model.event),
                runGateway.list(model.event),
                BiFunction { registrations: List<Registration>, runs: List<Run> ->
                    registrations to runs
                }
        ).subscribeOn(Schedulers.io())
                .observeOnFx()
                .subscribe { (registrations, runs) ->
                    model.event.registrations.setAll(registrations)
                    model.event.runs.setAll(runs)
                    runGateway.hydrateWithRegistrationMetadata(runs, registrations)
                    if (model.subscriber) {
                        reportService.generateAuditList(model.event)
                    }
                }
    }

    fun docked() {
        val watch = runGateway.watchList(model.event, model.event.registrations)
        model.disposables += watch
                .doOnDispose { println("RunEventController runGateway watchList doOnDispose()") }
                .subscribeOn(Schedulers.io())
                .observeOnFx()
                .doOnNextFx {
                    model.event.runForNextDriverBinding.invalidate()
                    model.event.runForNextTimeBinding.invalidate()
                }
                .subscribe(
                        entityWatchEventConsumer(
                                idProperty = Run::id,
                                list = model.event.runs
                        ),
                        Consumer { /* no-op */ }
                )
        if (model.subscriber) {
            model.disposables += watch
                    .observeOn(Schedulers.computation())
                    .subscribe { reportService.generateAuditList(model.event) }
        }
        model.disposables += registrationGateway.watchList(model.event)
                .doOnDispose { println("RunEventController registrationGateway watchList doOnDispose()") }
                .subscribeOn(Schedulers.io())
                .observeOnFx()
                .subscribe(
                        { registrations ->
                            model.event.registrations.setAll(registrations)
                            runGateway.hydrateWithRegistrationMetadata(model.event.runs, registrations, true)
                        },
                        { /* no-op */ }
                )
    }

    fun undocked() {
        println("RunEventController.undocked()")
        model.disposables.clear()
        if (timerService.model.timer != null) {
            timerService.stop()
        }
        scope.deregister()
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
            runService.addNextTime(model.event, input.et)
                    .subscribeOn(Schedulers.computation())
                    .observeOnFx()
                    .subscribe()
        }
    }

}
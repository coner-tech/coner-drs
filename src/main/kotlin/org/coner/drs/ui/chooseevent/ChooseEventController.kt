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

package org.coner.drs.ui.chooseevent

import com.github.thomasnield.rxkotlinfx.doOnSubscribeFx
import com.github.thomasnield.rxkotlinfx.observeOnFx
import com.github.thomasnield.rxkotlinfx.subscribeOnFx
import io.reactivex.functions.Consumer
import org.coner.drs.domain.entity.Event
import org.coner.drs.io.db.entityWatchEventConsumer
import org.coner.drs.io.gateway.EventGateway
import org.coner.drs.ui.addevent.AddEventWizard
import org.coner.drs.ui.main.ChangeToScreenEvent
import org.coner.drs.ui.main.Screen
import tornadofx.*

class ChooseEventController : Controller() {
    val model: ChooseEventModel by inject()
    val eventGateway: EventGateway by inject()

    fun loadEvents() {
        model.events.clear()
        model.events.addAll(eventGateway.list())
    }

    fun addEvent() {
        val wizard = find<AddEventWizard>(AddEventWizard.Scope(scope))
        wizard.onComplete {
            eventGateway.save(wizard.event.item)
        }
        wizard.openModal()
    }

    fun save(event: Event) {
        eventGateway.save(event)
    }

    fun onClickStart() {
        fire(ChangeToScreenEvent(Screen.RunEvent(model.choice)))
    }

    fun docked() {
        println("ChooseEventController.docked()")
        model.disposables.add(
                eventGateway.watchList()
                        .observeOnFx()
                        .subscribeOnFx()
                        .doOnSubscribe {
                            println("ChooseEventController watchList doOnSubscribe()")
                            loadEvents()
                            model.docked = true
                        }
                        .doOnComplete {
                            println("ChooseEventController watchList doOnComplete()")
                        }
                        .doOnDispose {
                            println("ChooseEventController watchList doOnDispose()")
                        }
                        .subscribe(
                                entityWatchEventConsumer(
                                        idProperty = Event::id,
                                        list = model.events
                                ),
                                Consumer {
                                    throw RuntimeException(it)
                                    /* no-op */
                                }
                        )
        )
    }

    fun undocked() {
        println("ChooseEventController.undocked()")
        model.disposables.clear()
        model.docked = false
    }
}
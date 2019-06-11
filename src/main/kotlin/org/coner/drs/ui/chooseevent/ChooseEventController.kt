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
                            model.events.clear()
                            model.events.addAll(eventGateway.list())
                        }
                        .doAfterNext {
                            model.docked = true
                            println("ChooseEventController.model.docked = ${model.docked}")
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
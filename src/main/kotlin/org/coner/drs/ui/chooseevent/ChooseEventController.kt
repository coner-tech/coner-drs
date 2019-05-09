package org.coner.drs.ui.chooseevent

import com.github.thomasnield.rxkotlinfx.observeOnFx
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

    fun init() {
        loadEvents()
    }

    fun loadEvents() {
        runAsync {
            eventGateway.list()
        } success {
            model.events.clear()
            model.events.addAll(it)
        }
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
        model.disposables.add(
                eventGateway.watchList()
                        .observeOnFx()
                        .subscribe(
                                entityWatchEventConsumer(
                                        idProperty = Event::id,
                                        list = model.events
                                ),
                                Consumer { /* no-op */ }
                        )
        )
    }

    fun undocked() {
        model.disposables.clear()
    }
}
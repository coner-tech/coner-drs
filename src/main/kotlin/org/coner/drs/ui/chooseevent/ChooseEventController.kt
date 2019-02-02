package org.coner.drs.ui.chooseevent

import com.github.thomasnield.rxkotlinfx.observeOnFx
import org.coner.drs.domain.entity.Event
import org.coner.drs.io.db.entityWatchEventConsumer
import org.coner.drs.io.service.EventIoService
import org.coner.drs.ui.addevent.AddEventWizard
import org.coner.drs.ui.main.ChangeToScreenEvent
import org.coner.drs.ui.main.Screen
import tornadofx.*

class ChooseEventController : Controller() {
    val model: ChooseEventModel by inject()
    val service: EventIoService by inject()

    fun init() {
        loadEvents()
    }

    fun loadEvents() {
        runAsync {
            service.list()
        } success {
            model.events.clear()
            model.events.addAll(it)
        }
    }

    fun addEvent() {
        val wizard = find<AddEventWizard>(AddEventWizard.Scope(scope))
        wizard.onComplete {
            service.save(wizard.event.item)
        }
        wizard.openModal()
    }

    fun save(event: Event) {
        service.save(event)
    }

    fun onClickStart() {
        fire(ChangeToScreenEvent(Screen.RunEvent(model.choice)))
    }

    fun docked() {
        model.disposables.add(service.watchList()
                .observeOnFx()
                .subscribe(entityWatchEventConsumer(
                        idProperty = Event::id,
                        list = model.events
                ))
        )
    }

    fun undocked() {
        model.disposables.clear()
    }
}
package org.coner.drs.ui.choose_event

import com.github.thomasnield.rxkotlinfx.observeOnFx
import org.coner.drs.Event
import org.coner.drs.io.db.entityWatchEventConsumer
import org.coner.drs.io.db.service.EventService
import org.coner.drs.ui.main.ChangeToScreenEvent
import org.coner.drs.ui.main.Screen
import tornadofx.*
import java.time.LocalDate

class ChooseEventController : Controller() {
    val model: ChooseEventModel by inject()
    val service: EventService by inject()

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
        val event = Event(
                date = LocalDate.now(),
                name = "New Event"
        )
        service.save(event)
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
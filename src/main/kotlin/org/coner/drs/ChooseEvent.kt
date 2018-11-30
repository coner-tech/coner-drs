package org.coner.drs

import com.github.thomasnield.rxkotlinfx.observeOnFx
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.layout.Priority
import tornadofx.*
import java.time.LocalDate
import tornadofx.getValue
import tornadofx.setValue
import io.reactivex.disposables.CompositeDisposable
import org.coner.drs.db.entityWatchEventConsumer
import org.coner.drs.db.service.EventService

class ChooseEventView : View("Events") {
    override val root = borderpane {
        center<ChooseEventTableView>()
        bottom<ChooseEventBottomView>()
    }
}

class ChooseEventTableView : View() {
    val model: ChooseEventModel by inject()
    val controller: ChooseEventController by inject()
    override val root = tableview(model.events) {
        column("Date", Event::dateProperty) {
            makeEditable()
        }
        column("Name", Event::nameProperty) {
            makeEditable()
        }
        onEditCommit {
            controller.save(it)
        }
        smartResize()
        bindSelected(model.choiceProperty)
    }

    init {
        controller.init()
    }

    override fun onDock() {
        super.onDock()
        controller.docked()
    }

    override fun onUndock() {
        super.onUndock()
        controller.undocked()
    }
}

class ChooseEventBottomView : View() {
    val model: ChooseEventModel by inject()
    val controller: ChooseEventController by inject()
    override val root = hbox {
        button("New") {
            action { controller.addEvent() }
        }
        pane {
            hgrow = Priority.ALWAYS
        }
        button("Start") {
            enableWhen(model.choiceProperty.isNotNull)
            action {
                controller.onClickStart()
            }
            isDefaultButton = true
        }
    }
}

class ChooseEventModel : ViewModel() {
    val events = observableList<Event>()
    val choiceProperty = SimpleObjectProperty<Event>(this, "event")
    var choice by choiceProperty

    val disposables = CompositeDisposable()
}

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

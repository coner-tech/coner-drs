package org.coner.drs

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.layout.Priority
import org.coner.drs.db.DrsIoController
import org.coner.drs.db.entity.EventDbEntity
import org.coner.drs.db.entity.toUiEntity
import org.coner.snoozle.db.jvm.watchListing
import tornadofx.*
import java.time.LocalDate
import tornadofx.getValue
import tornadofx.setValue
import io.reactivex.schedulers.Schedulers
import io.reactivex.disposables.CompositeDisposable
import org.coner.drs.db.EntityWatchEvent
import org.coner.drs.db.entity.toDbEntity
import org.coner.drs.db.service.EventService
import java.nio.file.StandardWatchEventKinds

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
                .subscribe { event ->
                    val considerForAddKinds = arrayOf(
                            StandardWatchEventKinds.ENTRY_CREATE,
                            StandardWatchEventKinds.ENTRY_MODIFY
                    )
                    if (considerForAddKinds.contains(event.watchEvent.kind()) && event.entity != null) {
                        val index = model.events.indexOfFirst { it.id == event.id }
                        if (index >= 0) {
                            model.events[index] = event.entity
                        } else {
                            model.events.add(event.entity)
                        }
                    } else if (event.watchEvent.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
                        val index = model.events.indexOfFirst { it.id == event.id }
                        if (index >= 0) {
                            model.events.removeAt(index)
                        }
                    }
                })
    }

    fun undocked() {
        model.disposables.clear()
    }
}

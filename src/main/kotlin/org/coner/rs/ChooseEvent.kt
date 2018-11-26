package org.coner.rs

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.layout.Priority
import tornadofx.*
import java.time.LocalDate
import tornadofx.getValue
import tornadofx.setValue
import java.util.concurrent.ThreadLocalRandom
import kotlin.random.Random
import kotlin.random.nextInt

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
        column("Date", Event::dateProperty)
        column("Name", Event::nameProperty)
        smartResize()
        bindSelected(model.choiceProperty)
    }

    init {
        controller.init()
    }
}

class ChooseEventBottomView : View() {
    val model: ChooseEventModel by inject()
    val controller: ChooseEventController by inject()
    override val root = hbox {
        button("New") {
            action { /* TODO */ }
        }
        pane {
            hgrow = Priority.ALWAYS
        }
        button("Start") {
            enableWhen(model.choiceProperty.isNotNull)
            action {
                controller.onClickStart()
            }
        }
    }
}

class ChooseEventModel : ViewModel() {
    val events = observableList<Event>()
    val choiceProperty = SimpleObjectProperty<Event>(this, "event")
    var choice by choiceProperty

}

class ChooseEventController : Controller() {
    val model: ChooseEventModel by inject()

    fun init() {
        runAsync {
            buildRandomEvents()
        } ui {
            model.events.addAll(it)
        }
    }

    private fun buildRandomEvents(): List<Event> {
        val events = mutableListOf<Event>()
        val now = LocalDate.now()
        val categories = listOf(
                Category(name = "Open"),
                Category(name = "Novice"),
                Category(name = "Pro")
        )
        val handicaps = arrayOf(
                "SS",
                "AS",
                "BS",
                "CS",
                "DS",
                "ES",
                "FS",
                "GS",
                "HS"
        ).map { Handicap(name = it) }
        val random = ThreadLocalRandom.current()

        for (i in 0..10) {
            val event = Event(date = now.minusMonths(i.toLong()), name = "Event $i")
            event.categories.addAll(categories)
            event.handicaps.addAll(handicaps)
            for (j in 0..100) {
                event.registrations.add(
                        Registration(event = event).apply {
                            category = categories.random()
                            handicap = handicaps.random()
                            number = random.nextInt(0, 999).toString()
                        }
                )
            }
            events += event
        }
        return events
    }

    fun onClickStart() {
        fire(ChangeToScreenEvent(Screen.RunEvent(model.choice)))
    }
}

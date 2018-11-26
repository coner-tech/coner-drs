package org.coner.rs

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.transformation.SortedList
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class RunEventFragment : Fragment() {
    val event: Event by param()
    val eventScope = Scope()

    val model: RunEventModel by inject(eventScope)

    override val root = titledpane(event.name) {
        isCollapsible = false
        borderpane {
            center { add(find<RunEventTableView>(eventScope)) }
            bottom { add(find<RunEventBottomView>(eventScope)) }
        }
    }

    init {
        model.event = event
    }
}

class RunEventTableView : View() {
    val model: RunEventModel by inject()

    override val root = tableview(SortedList(model.runs)) {
        isEditable = true
        setSortPolicy { false }
        readonlyColumn("Sequence", Run::sequence)
        column("Registration", Run::registrationProperty) {
        }
        column("Time", Run::rawTime) {
            makeEditable()
        }
        column("Cones", Run::cones) {
            makeEditable()
        }
        column("Did Not Finish", Run::didNotFinish) {
            makeEditable()
        }
        column("Disqualified", Run::disqualified) {
            makeEditable()
        }
        column("Re-Run", Run::rerun) {
            makeEditable()
        }
        smartResize()
    }

    init {
        subscribe<FocusAndEditRunEvent> { onFocusAndEditRunEvent(it) }
    }

    fun onFocusAndEditRunEvent(event: FocusAndEditRunEvent) {
        val row = root.items.indexOf(event.run)
        root.selectionModel.select(event.run)
        root.edit(row, root.columns[1])
    }
}

class RunEventBottomView : View() {
    val controller: RunEventController by inject()
    override val root = hbox {
        button("Add Run") {
            shortcut("Ctrl+Enter")
            action { controller.addRun() }
        }
    }
}

class RunEventModel : ViewModel() {
    val runs = observableList<Run>()
    val eventProperty = SimpleObjectProperty<Event>()
    var event by eventProperty

}

class RunEventController : Controller() {
    val model: RunEventModel by inject()

    fun addRun() {
        val run = Run(event = model.event)
        run.sequence = 1 + model.runs.size
        model.runs.add(run)
    }
}

class FocusAndEditRunEvent(val run: Run) : FXEvent()
package org.coner.drs.ui.runevent

import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import org.coner.drs.Event
import tornadofx.*

class RunEventFragment : Fragment("Run Event") {
    val event: Event by param()
    val eventScope = Scope()

    val model: RunEventModel by inject(eventScope)
    val controller: RunEventController by inject(eventScope)

    init {
        model.event = event
        controller.init()
    }

    override val root = titledpane(event.name) {
        isCollapsible = false
        prefHeightProperty().bind(parentProperty().select { (it as Region).heightProperty() })
        borderpane {
            left { add(find<RunEventLeftDrawerView>(eventScope)) }
            right { add(find<RunEventRightDrawerView>(eventScope)) }
            center {
                vgrow = Priority.ALWAYS
                add(find<RunEventTableView>(eventScope))
            }
        }
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
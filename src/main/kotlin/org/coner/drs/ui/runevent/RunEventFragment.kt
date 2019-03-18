package org.coner.drs.ui.runevent

import javafx.geometry.Orientation
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.RunEvent
import tornadofx.*

class RunEventFragment : Fragment("Run Event") {
    val event: RunEvent by param()
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
            left {
                add(find<AddNextDriverView>(eventScope))
            }
            center {
                add(find<RunEventTableView>(eventScope))
            }
            right { add(find<RunEventRightDrawerView>(eventScope)) }
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
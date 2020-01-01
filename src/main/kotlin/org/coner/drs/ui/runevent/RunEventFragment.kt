package org.coner.drs.ui.runevent

import javafx.scene.layout.Region
import org.coner.drs.domain.entity.RunEvent
import tornadofx.*

class RunEventFragment : Fragment("Run Event") {
    val event: RunEvent by param()
    val subscriber: Boolean by param()
    val eventScope = Scope()

    val model: RunEventModel by inject(eventScope)
    val controller: RunEventController by inject(eventScope)

    init {
        model.event = event
        model.subscriber = subscriber
        controller.init()
    }

    override val root = borderpane {
        id = "run-event"
        top {
            add(find<RunEventTopView>(eventScope))
        }
        left {
            add(find<AddNextDriverView>(eventScope))
        }
        center {
            add(find<RunEventTableView>(eventScope))
        }
        right { add(find<RunEventRightDrawerView>(eventScope)) }
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
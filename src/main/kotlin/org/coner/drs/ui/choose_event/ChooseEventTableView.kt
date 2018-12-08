package org.coner.drs.ui.choose_event

import org.coner.drs.Event
import tornadofx.*

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
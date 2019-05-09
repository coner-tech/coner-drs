package org.coner.drs.ui.chooseevent

import tornadofx.*

class ChooseEventView : View("Choose Event") {

    init {
        println("ChooseEventView.init()")
    }

    override val root = borderpane() {
        id = "choose-event"
    }

    override fun onDock() {
        super.onDock()
        println("ChooseEventView.onDock()")
        root.center<ChooseEventTableView>()
        root.bottom<ChooseEventBottomView>()
    }

    override fun onUndock() {
        super.onUndock()
        println("ChooseEventView.onUndock()")
        root.center = null
        root.bottom = null
    }
}
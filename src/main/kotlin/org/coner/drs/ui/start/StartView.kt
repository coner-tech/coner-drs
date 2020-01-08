package org.coner.drs.ui.start

import tornadofx.*

class StartView : View("Start") {
    val controller: StartController by inject()
    val model: StartModel by inject()

    override val root = borderpane {
        top<StartTopView>()
        center<StartCenterView>()
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
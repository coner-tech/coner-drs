package org.coner.drs.ui.home

import org.coner.drs.ui.chooseevent.ChooseEventView
import tornadofx.*
import java.io.File
import java.nio.file.Path

class HomeView : View() {

    override val root = stackpane()

    override fun onDock() {
        super.onDock()
        root.add(find<ChooseEventView>())
    }

    override fun onUndock() {
        super.onUndock()
        root.replaceChildren()
    }

}


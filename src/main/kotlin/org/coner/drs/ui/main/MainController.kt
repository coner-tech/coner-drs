package org.coner.drs.ui.main

import org.coner.drs.io.DrsIoController
import org.coner.drs.ui.run_event.RunEventFragment
import org.coner.drs.ui.start.StartView
import org.coner.drs.ui.choose_event.ChooseEventView
import tornadofx.*

class MainController : Controller() {
    val model: MainModel by inject()
    val drsIo: DrsIoController by inject()

    fun onChangeToScreen(screen: Screen): UIComponent {
        val uiComponent = when (screen) {
            is Screen.Start -> find<StartView>()
            is Screen.ChooseEvent -> {
                if (model.screen == Screen.Start) {
                    drsIo.open(
                            pathToDrsDatabase = screen.pathToDrsDb,
                            pathToCrispyFishDatabase = screen.pathToCfDb
                    )
                }
                find<ChooseEventView>()
            }
            is Screen.RunEvent -> find<RunEventFragment>(RunEventFragment::event to screen.event)
        }
        model.screen = screen
        return uiComponent
    }
}
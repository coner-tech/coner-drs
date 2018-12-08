package org.coner.drs.ui.start

import org.coner.drs.ui.main.ChangeToScreenEvent
import org.coner.drs.ui.main.Screen
import tornadofx.*

class StartController : Controller() {
    val model: StartModel by inject()

    fun onClickChooseRawSheetDatabase() {
        val dir = chooseDirectory("Raw Sheet Database") ?: return
        model.rawSheetDatabase = dir
    }

    fun onClickStart() {
        fire(ChangeToScreenEvent(Screen.ChooseEvent(model.rawSheetDatabase)))
    }
}
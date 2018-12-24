package org.coner.drs.ui.start

import org.coner.drs.ui.main.ChangeToScreenEvent
import org.coner.drs.ui.main.Screen
import tornadofx.*

class StartController : Controller() {
    val model: StartModel by inject()

    fun onClickChooseRawSheetDatabase() {
        val dir = chooseDirectory("Coner Digital Raw Sheet Database") ?: return
        model.rawSheetDatabase = dir
    }

    fun onClickChooseCrispyFishDatabase() {
        val dir = chooseDirectory("Crispy Fish Database") ?: return
        model.crispyFishDatabase = dir
    }

    fun onClickStart() {
        fire(ChangeToScreenEvent(Screen.ChooseEvent(
                pathToDrsDb = model.rawSheetDatabase,
                pathToCfDb = model.crispyFishDatabase
        )))
    }
}
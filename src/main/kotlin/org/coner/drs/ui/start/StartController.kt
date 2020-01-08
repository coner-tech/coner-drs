package org.coner.drs.ui.start

import com.github.thomasnield.rxkotlinfx.actionEvents
import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import javafx.application.Platform
import org.coner.drs.ui.main.ChangeToScreenEvent
import org.coner.drs.ui.main.Screen
import tornadofx.*

class StartController : Controller() {
    val model: StartModel by inject()

    private val disposables = CompositeDisposable()

    fun onClickChooseRawSheetDatabase() {
        val dir = chooseDirectory("Coner Digital Raw Sheet Database") ?: return
        model.rawSheetDatabase = dir.toPath()
    }

    fun onClickChooseCrispyFishDatabase() {
        val dir = chooseDirectory("Crispy Fish Database") ?: return
        model.crispyFishDatabase = dir
    }

    fun onClickStart() {
        model.commit {
            fire(ChangeToScreenEvent(Screen.ChooseEvent(
                    pathToDrsDb = model.rawSheetDatabase,
                    pathToCfDb = model.crispyFishDatabase
            )))
        }
    }

    fun docked() {
        find<StartCenterView>().apply {
            disposables += rawSheetDatabaseChooseButton.actionEvents()
                    .observeOnFx()
                    .subscribe { onClickChooseRawSheetDatabase() }
            disposables += crispyFishDatabaseChooseButton.actionEvents()
                    .observeOnFx()
                    .subscribe { onClickChooseCrispyFishDatabase() }
            disposables += startButton.actionEvents()
                    .observeOnFx()
                    .subscribe { onClickStart() }
        }
        find<StartTopView>().apply {
            disposables += fileExit.actionEvents()
                .subscribe { Platform.exit() }
            disposables += helpAbout.actionEvents()
                    .subscribe { TODO() }
        }
    }

    fun undocked() {
        disposables.clear()
    }


}
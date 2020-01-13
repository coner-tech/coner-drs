/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
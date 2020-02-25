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

package org.coner.drs.ui.main

import org.coner.drs.di.katanaAppComponent
import org.coner.drs.io.DrsIoController
import org.coner.drs.io.gateway.EventGateway
import org.coner.drs.ui.home.HomeFragment
import org.coner.drs.ui.runevent.RunEventFragment
import org.coner.drs.ui.start.StartView
import org.coner.drs.ui.home.HomeScope
import org.coner.drs.ui.start.StartModel
import tornadofx.*

class MainController : Controller() {
    val view: MainView by inject()
    val model: MainModel by inject()
    val eventGateway: EventGateway by inject()

    fun onChangeToScreen(event: ChangeToScreenEvent) {
        println("MainView#${model.id} onChangeToScreen: ${event.screen}")
        val newScreen = findUiComponentForScreen(event.screen)
        if (view.root.children.isEmpty()) {
            println("Adding ${newScreen.javaClass.simpleName}")
            view.root.add(newScreen.root)
        } else {
            println("Replacing children with ${newScreen.javaClass.simpleName}")
            view.root.replaceChildren(newScreen)
        }
        view.titleProperty.unbind()
        view.titleProperty.bind(newScreen.titleProperty)
    }

    private fun findUiComponentForScreen(screen: Screen): UIComponent {
        val uiComponent = when (screen) {
            is Screen.Start -> find<StartView>()
            is Screen.Home -> {
                HomeFragment.find(
                        uiComponent = this,
                        katanaScope = HomeScope(
                                appComponent = katanaAppComponent,
                                pathToDigitalRawSheetsDatabase = screen.pathToDrsDb
                        )
                ).apply {
                    if (model.screen == Screen.Start) {
                        prepareDrsIo(
                                pathToDrsDb = screen.pathToDrsDb,
                                pathToCfDb = screen.pathToCfDb
                        )
                    }
                }
            }
            is Screen.RunEvent -> {
                val runEvent = eventGateway.asRunEvent(screen.event)
                find<RunEventFragment>(
                        RunEventFragment::event to runEvent,
                        RunEventFragment::subscriber to find<StartModel>().subscriber
                )
            }
        }
        model.screen = screen
        return uiComponent
    }

    fun onDock() {
        onChangeToScreen(ChangeToScreenEvent(Screen.Start))
        runLater {
            model.busRegistrations.add(subscribe<ChangeToScreenEvent> { onChangeToScreen(it) })
        }
    }

    fun onUndock() {
        model.busRegistrations.forEach { it.unsubscribe() }
    }
}
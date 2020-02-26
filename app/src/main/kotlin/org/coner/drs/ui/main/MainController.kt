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
import org.coner.drs.di.katanaScopes
import org.coner.drs.ui.home.HomeFragment
import org.coner.drs.ui.home.HomeKatanaScope
import org.coner.drs.ui.runevent.RunEventFragment
import org.coner.drs.ui.runevent.RunEventKatanaScope
import org.coner.drs.ui.start.StartView
import org.coner.drs.ui.start.StartModel
import tornadofx.*

class MainController : Controller() {
    val view: MainView by inject()
    val model: MainModel by inject()

    private fun onChangeToScreen(event: ChangeToScreenEvent) {
        println("MainView#${model.id} onChangeToScreen: ${event.screen}")
        val newScreen = findUiComponentForScreen(event.screen)
        beforeScreenChange(event, newScreen)
        performScreenChange(newScreen)
        afterScreenChange(event)
        model.screen = event.screen
        model.screenUiComponent = newScreen
    }

    private fun beforeScreenChange(event: ChangeToScreenEvent, uiComponent: UIComponent) {
        when {
            model.screen is Screen.Start && event.screen is Screen.Home -> {
                // Start => Home
                katanaScopes.home = HomeKatanaScope(
                        appComponent = katanaAppComponent,
                        pathToDigitalRawSheetsDatabase = event.screen.pathToDrsDb
                )
                (uiComponent as HomeFragment).prepareDrsIo(
                        pathToDrsDb = event.screen.pathToDrsDb,
                        pathToCfDb = event.screen.pathToCfDb
                )
            }
            model.screen is Screen.Home && event.screen is Screen.RunEvent -> {
                // Home => RunEvent
                katanaScopes.runEvent = RunEventKatanaScope(
                        katanaScopes = katanaScopes
                )
            }
        }
    }

    private fun performScreenChange(newScreen: UIComponent) {
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

    private fun afterScreenChange(event: ChangeToScreenEvent) {
        when {
            event.screen is Screen.Start && model.screen is Screen.Home -> {
                // Start <= Home
                katanaScopes.home = null
                model.screenUiComponent.scope.deregister()
            }
            event.screen is Screen.Home && model.screen is Screen.RunEvent -> {
                // Home <= RunEvent
                katanaScopes.runEvent = null
                model.screenUiComponent.scope.deregister()
            }
        }
    }

    private fun findUiComponentForScreen(screen: Screen): UIComponent {
        return when (screen) {
            is Screen.Start -> find<StartView>()
            is Screen.Home -> HomeFragment.create(
                    component = this,
                    pathToDigitalRawSheetsDatabase = screen.pathToDrsDb
            )
            is Screen.RunEvent -> RunEventFragment.create(
                    component = this,
                    eventId = screen.event.id,
                    subscriber = find<StartModel>().subscriber
            )
        }
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
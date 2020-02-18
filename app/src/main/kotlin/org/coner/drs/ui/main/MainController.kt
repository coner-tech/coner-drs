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

import org.coner.drs.io.DrsIoController
import org.coner.drs.io.gateway.EventGateway
import org.coner.drs.ui.runevent.RunEventFragment
import org.coner.drs.ui.start.StartView
import org.coner.drs.ui.chooseevent.ChooseEventView
import org.coner.drs.ui.start.StartModel
import tornadofx.*

class MainController : Controller() {
    val model: MainModel by inject()
    val drsIo: DrsIoController by inject()
    val eventGateway: EventGateway by inject()

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
}
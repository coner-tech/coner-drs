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

package org.coner.drs.test.page.real

import org.coner.drs.test.page.RunEventRightDrawerPage
import org.coner.drs.test.page.TimerPage
import org.testfx.api.FxRobot
import tornadofx.*

class RealRunEventRightDrawerPage(val robot: FxRobot) : RunEventRightDrawerPage {

    override fun root(): Drawer = robot.lookup("#run-event-right-drawer").query()

    override fun timerDrawerItem(): DrawerItem = root().items.single { it.id == "timer-item" }

    override fun expandTimer() {
        val item = timerDrawerItem()
        check(!item.expanded) { "Timer already expanded" }
        FX.runAndWait { item.expanded = true }
    }

    override fun collapseTimer() {
        val item = timerDrawerItem()
        check(item.expanded) { "Timer already collapsed" }
        FX.runAndWait { item.expanded = false }
    }

    override fun toTimerPage(): TimerPage {
        return RealTimerPage(robot)
    }

}

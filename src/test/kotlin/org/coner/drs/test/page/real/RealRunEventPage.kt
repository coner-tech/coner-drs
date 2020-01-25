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

import javafx.scene.control.*
import org.coner.drs.test.page.*
import org.testfx.api.FxRobot

open class RealRunEventPage(protected val robot: FxRobot) : RunEventPage {

    override fun root() = robot.lookup("#run-event")
            .query() as TitledPane

    override fun toAddNextDriverPage(): AddNextDriverPage {
        return RealAddNextDriverPage(robot)
    }

    override fun toTablePage(): RunEventTablePage {
        return RealRunEventTablePage(robot)
    }

    override fun toRightDrawerPage(): RunEventRightDrawerPage {
        return RealRunEventRightDrawerPage(robot)
    }

    override fun onTopPage(fn: RunEventTopPage.() -> Unit) {
        RealRunEventTopPage(robot)
    }
}
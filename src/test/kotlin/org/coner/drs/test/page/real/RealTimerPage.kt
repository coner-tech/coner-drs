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

import javafx.scene.Node
import javafx.scene.control.Button
import org.coner.drs.test.page.TimerConfigurationPage
import org.coner.drs.test.page.TimerPage
import org.hamcrest.CoreMatchers
import org.testfx.api.FxRobot
import org.testfx.matcher.base.GeneralMatchers
import org.testfx.matcher.base.NodeMatchers
import org.testfx.matcher.control.ButtonMatchers
import org.testfx.matcher.control.ComboBoxMatchers
import org.testfx.matcher.control.LabeledMatchers
import org.testfx.matcher.control.TextMatchers
import tornadofx.*

class RealTimerPage(val robot: FxRobot) : TimerPage {
    override fun root() = robot.lookup("#timer").query() as Form

    override fun configureButton(): Button = robot.from(root())
            .lookup("#configure-button")
            .queryButton()

    override fun pressConfigure() {
        robot.clickOn(configureButton())
    }

    override fun startButton(): Button {
        return robot.from(root())
                .lookup("#operation-button")
                .match(LabeledMatchers.hasText("Start"))
                .query()
    }

    override fun stopButton(): Button {
        return robot.from(root())
                .lookup("#operation-button")
                .match(LabeledMatchers.hasText("Stop"))
                .query()
    }

    override fun pressStart() {
        robot.clickOn(startButton())
    }

    override fun pressStop() {
        robot.clickOn(stopButton())
    }

    override fun toTimerConfigurationPage(): TimerConfigurationPage {
        return RealTimerConfigurationPage(robot)
    }
}

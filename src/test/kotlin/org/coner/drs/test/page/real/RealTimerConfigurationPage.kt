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

import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import org.coner.drs.test.page.TimerConfigurationPage
import org.coner.drs.test.testfx.chooseInChoiceBox
import org.coner.drs.ui.TimerConfigurationConverter
import org.testfx.api.FxRobot
import org.testfx.matcher.control.LabeledMatchers
import tornadofx.*
import java.nio.file.Path
import kotlin.reflect.KClass

class RealTimerConfigurationPage(val robot: FxRobot) : TimerConfigurationPage {

    private val converter = TimerConfigurationConverter()

    override fun root(): Form = robot.lookup("#timer-configuration").query()

    override fun typeChoiceBox(): ChoiceBox<KClass<*>> = robot.from(root()).lookup("#type").query()

    override fun chooseType(timerConfiguration: KClass<*>) {
        val match = converter.toString(timerConfiguration)
        robot.chooseInChoiceBox(typeChoiceBox(), LabeledMatchers.hasText(match))
    }

    override fun applyButton(): Button = robot.from(root()).lookup("#apply").query()

    override fun pressApply() {
        robot.clickOn(applyButton())
    }

    override fun fileTextField(): TextField = robot.from(root()).lookup("#file-textfield").query()

    override fun chooseFile(path: Path) {
        FX.runAndWait {
            fileTextField().text = path.toString()
        }
    }

}

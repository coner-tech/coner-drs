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
import javafx.scene.control.ButtonBar
import javafx.scene.layout.BorderPane
import org.coner.drs.test.page.AlterDriverSequencePage
import org.coner.drs.test.page.PreviewAlteredDriverSequencePage
import org.coner.drs.test.page.SpecifyDriverSequenceAlterationPage
import org.testfx.api.FxRobot

open class RealAlterDriverSequencePage(protected val robot: FxRobot) : AlterDriverSequencePage {

    override fun root() = robot.lookup("#alter-driver-sequence-view").query<BorderPane>()

    override fun bottomButtonBar() = robot.from(root()).lookup("#bottom-buttonbar").query<ButtonBar>()

    override fun okButton() = robot.from(bottomButtonBar()).lookup("#ok").query<Button>()

    override fun cancelButton() = robot.from(bottomButtonBar()).lookup("#cancel").query<Button>()

    override fun clickOkButton() {
        robot.clickOn(okButton())
    }

    override fun clickCancelButton() {
        robot.clickOn(cancelButton())
    }

    override fun toSpecifyDriverSequenceAlterationPage(): SpecifyDriverSequenceAlterationPage {
        return RealSpecifyDriverSequenceAlterationPage(robot)
    }

    override fun toPreviewAlteredDriverSequencePage(): PreviewAlteredDriverSequencePage {
        return RealPreviewAlteredDriverSequencePage(robot)
    }
}
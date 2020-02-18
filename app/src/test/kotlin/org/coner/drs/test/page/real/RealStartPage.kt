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
import javafx.scene.control.TextInputControl
import javafx.scene.layout.StackPane
import org.coner.drs.test.page.StartPage
import org.testfx.api.FxRobot
import java.io.File
import java.nio.file.Path

open class RealStartPage(protected val robot: FxRobot) : StartPage {

    override fun root() = robot.lookup("#start").query() as StackPane

    override fun rawSheetDatabaseField() = robot.from(root())
            .lookup("#raw-sheet-database-field")
            .query() as TextInputControl

    override fun setRawSheetDatabase(file: Path) {
        throw UnsupportedOperationException()
    }

    override fun crispyFishDatabaseField() = robot.from(root())
            .lookup("#crispy-fish-database-field")
            .query() as TextInputControl

    override fun setCrispyFishDatabase(file: File) {
        throw UnsupportedOperationException()
    }

    override fun startButton() = robot.from(root())
            .lookup("#start-button")
            .query() as Button

    override fun clickStartButton() {
        robot.clickOn(startButton())
    }
}
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
import javafx.scene.control.TableCell
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
import org.awaitility.Awaitility
import org.awaitility.Awaitility.await
import org.coner.drs.domain.entity.Run
import org.coner.drs.test.page.AlterDriverSequencePage
import org.coner.drs.test.page.RunEventTablePage
import org.testfx.api.FxRobot
import tornadofx.*

open class RealRunEventTablePage(private val robot: FxRobot) : RunEventTablePage {

    override fun root() = robot.lookup("#run-event-table")
            .query<Form>()

    override fun runsTable() = robot.from(root())
            .lookup("#runs-table")
            .query<TableView<Run>>()

    override fun tableCellForSequence(sequence: Int): TableCell<Run, Int> {
        return robot.from(runsTable())
                .lookup<TableCell<Run, Int>> { it.text == sequence.toString() }
                .query()
    }

    override fun selectSequence(sequence: Int) {
        val cell = tableCellForSequence(sequence)
        robot.clickOn(cell)
    }

    override fun keyboardShortcutInsertDriverIntoSequence(sequence: Int): AlterDriverSequencePage {
        check(runsTable().selectionModel.selectedItem?.sequence == sequence) {
            "Select run with sequence prior to use"
        }
        robot.press(KeyCode.CONTROL)
        robot.type(KeyCode.INSERT)
        robot.release(KeyCode.CONTROL)
        return RealAlterDriverSequencePage(robot)
    }

    override fun keyboardShortcutClearTime(sequence: Int) {
        val cell = tableCellForSequence(sequence)
        robot.clickOn(cell)
        robot.press(KeyCode.CONTROL)
        robot.type(KeyCode.C)
        robot.release(KeyCode.CONTROL)
    }

    override fun keyboardShortcutDeleteRun(sequence: Int) {
        val cell = tableCellForSequence(sequence)
        robot.clickOn(cell)
        robot.press(KeyCode.CONTROL)
        robot.type(KeyCode.DELETE)
        robot.release(KeyCode.CONTROL)
    }
}
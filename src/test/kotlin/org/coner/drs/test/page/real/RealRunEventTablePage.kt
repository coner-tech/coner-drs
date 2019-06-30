package org.coner.drs.test.page.real

import javafx.scene.control.TableCell
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import javafx.scene.input.KeyCombination
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

    override fun clickInsertDriverIntoSequence(sequence: Int): AlterDriverSequencePage {
        val cell = tableCellForSequence(sequence)
        robot.rightClickOn(cell)
        robot.clickOn("Driver")
        robot.clickOn("Insert Driver Into Sequence")
        return RealAlterDriverSequencePage(robot)
    }

    override fun keyboardShortcutInsertDriverIntoSequence(sequence: Int) {
        check(runsTable().selectionModel.selectedItem.sequence == sequence) {
            "Select run with sequence prior to use"
        }
        robot.press(KeyCode.CONTROL)
        robot.type(KeyCode.INSERT)
        robot.release(KeyCode.CONTROL)
    }
}
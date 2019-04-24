package org.coner.drs.test.page.real

import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import org.coner.drs.domain.entity.Run
import org.coner.drs.test.page.RunEventPage
import org.testfx.api.FxRobot
import tornadofx.*

open class RealRunEventPage(protected val robot: FxRobot) : RunEventPage {

    override fun root() = robot.lookup("#run-event")
            .query() as TitledPane

    override fun addNextDriverForm() = robot.from(root())
            .lookup("#add-next-driver")
            .query() as Form

    override fun addNextDriverNumbersField() = robot.from(addNextDriverForm())
            .lookup("#numbers")
            .query() as TextField

    override fun clickOnAddNextDriverNumbersField() {
        robot.clickOn(addNextDriverNumbersField())
    }

    override fun fillAddNextDriverNumbersField(numbers: String) {
        clickOnAddNextDriverNumbersField()
        robot.write(numbers)
    }

    override fun addNextDriverAddButton() = robot.from(addNextDriverForm())
            .lookup("#add")
            .query() as SplitMenuButton

    override fun clickOnAddNextDriverAddButton() {
        robot.clickOn(addNextDriverAddButton())
    }

    override fun runsTable() = robot.from(root())
            .lookup("#runs-table")
            .query() as TableView<Run>
}
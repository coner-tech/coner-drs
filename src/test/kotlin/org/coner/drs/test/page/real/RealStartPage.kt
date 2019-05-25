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
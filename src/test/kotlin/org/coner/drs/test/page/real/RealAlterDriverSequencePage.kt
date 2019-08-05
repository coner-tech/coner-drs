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
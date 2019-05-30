package org.coner.drs.test.page.real

import javafx.scene.control.ToggleButton
import javafx.scene.input.KeyCode
import org.coner.drs.domain.entity.Registration
import org.coner.drs.test.page.SpecifyDriverSequenceAlterationPage
import org.testfx.api.FxRobot
import tornadofx.*

open class RealSpecifyDriverSequenceAlterationPage(
        protected val robot: FxRobot
) : SpecifyDriverSequenceAlterationPage {

    override fun root() = robot.lookup("#specify-driver-sequence-alteration")
            .queryAs(Form::class.java)!!

    override fun relativeBefore() = robot.from(root())
            .lookup("#relative-before")
            .query() as ToggleButton

    override fun relativeAfter() = robot.from(root())
            .lookup("#relative-after")
            .query() as ToggleButton

    override fun selectRelativeBefore() {
        robot.clickOn(relativeBefore())
    }

    override fun selectRelativeAfter() {
        robot.clickOn(relativeAfter())
    }

    override fun numbersField() = robot.from(root())
            .lookup("#numbers")
            .queryTextInputControl()!!

    override fun focusNumbersField() {
        robot.clickOn(numbersField())
    }

    override fun writeInNumbersField(s: String) {
        focusNumbersField()
        robot.write(s)
    }

    override fun registrationsListView() = robot.from(root())
            .lookup("#registrations-list-view")
            .queryListView<Registration>()!!

    override fun selectRegistration(registration: Registration) {
        focusNumbersField()
        while (registrationsListView().selectionModel.selectedItem != registration) {
            val lastSelectedItem = registrationsListView().selectionModel.selectedItem
            robot.type(KeyCode.DOWN)
            if (registrationsListView().selectionModel.selectedItem == lastSelectedItem)
                throw Exception("Reached end of registrations without finding a match")
        }
    }

}
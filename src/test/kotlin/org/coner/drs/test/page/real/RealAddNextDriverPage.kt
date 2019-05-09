package org.coner.drs.test.page.real

import javafx.scene.control.SplitMenuButton
import javafx.scene.input.KeyCode
import org.coner.drs.domain.entity.Registration
import org.coner.drs.test.page.AddNextDriverPage
import org.testfx.api.FxRobot
import tornadofx.*

open class RealAddNextDriverPage(
        protected val robot: FxRobot
) : AddNextDriverPage {

    override fun root() = robot.lookup("#add-next-driver")
            .queryAs(Form::class.java)!!

    override fun numbersField() = robot.from(root())
            .lookup("#numbers")
            .queryTextInputControl()!!

    override fun focusNumbersField() {
        robot.press(KeyCode.ALT)
        robot.type(KeyCode.N)
        robot.release(KeyCode.ALT)
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
            robot.press(KeyCode.DOWN)
            if (registrationsListView().selectionModel.selectedItem == lastSelectedItem)
                throw Exception("Reached end of registrations without finding a match")
        }
    }

    override fun addButton() = robot.from(root())
            .lookup("#add")
            .queryAs(SplitMenuButton::class.java)!!

    override fun doAddSelectedRegistration() {
        robot.clickOn(addButton())
    }

    override fun addForceExactNumbersItem() = addButton().items.first { it.id == "force-exact-numbers" }

    override fun doAddForceExactNumbers() {
        robot.press(KeyCode.CONTROL)
        robot.type(KeyCode.ENTER)
        robot.release(KeyCode.CONTROL)
    }
}
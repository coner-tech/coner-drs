package org.coner.drs.test.page

import javafx.scene.control.SplitMenuButton
import javafx.scene.input.KeyCode
import org.coner.drs.domain.entity.Registration
import org.testfx.api.FxRobot
import tornadofx.*

class AddNextDriverPage(private val robot: FxRobot) {

    fun root() = robot.lookup("#add-next-driver").queryAs(Form::class.java)

    fun numbersField() = robot.from(root()).lookup("#numbers").queryTextInputControl()

    fun registrationsListView() = robot.from(root().lookup("#registrations-list-view")).queryListView<Registration>()

    fun doNumbersFieldFocusKeyboardShortcut() {
        robot.press(KeyCode.ALT)
        robot.type(KeyCode.N)
        robot.release(KeyCode.ALT)
    }

    fun addButton() = robot.from(root().lookup("#add")).queryAs(SplitMenuButton::class.java)

    fun doAddExactNumbersKeyboardShortcut() {
        robot.press(KeyCode.CONTROL)
        robot.type(KeyCode.ENTER)
        robot.release(KeyCode.CONTROL)
    }


}
package org.coner.drs.test.page.real

import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import org.coner.drs.test.page.TimerConfigurationPage
import org.coner.drs.ui.TimerConfigurationConverter
import org.testfx.api.FxRobot
import tornadofx.*
import java.nio.file.Path
import kotlin.reflect.KClass

class RealTimerConfigurationPage(val robot: FxRobot) : TimerConfigurationPage {

    private val converter = TimerConfigurationConverter()

    override fun root(): Form = robot.lookup("#timer-configuration").query()

    override fun typeChoiceBox(): ChoiceBox<KClass<*>> = robot.from(root()).lookup("#type").query()

    override fun chooseType(timerConfiguration: KClass<*>) {
        // TODO: extract to a util or testfx contribution
        val match = converter.toString(timerConfiguration)
        var matched = false
        val max = 3
        var attempts = 0
        val label = robot.from(typeChoiceBox()).lookup(".label").query<Label>()
        while (!matched) {
            if (attempts > max) {
                throw IllegalStateException("Exceeded max attempts")
            }
            robot.clickOn(typeChoiceBox())
            robot.type(KeyCode.DOWN)
            robot.type(KeyCode.ENTER)
            matched = label.text == match
            attempts++
        }
    }

    override fun applyButton(): Button = robot.from(root()).lookup("#apply").query()

    override fun pressApply() {
        robot.clickOn(applyButton())
    }

    override fun fileTextField(): TextField = robot.from(root()).lookup("#file-textfield").query()

    override fun chooseFile(path: Path) {
        FX.runAndWait {
            fileTextField().text = path.toString()
        }
    }

}

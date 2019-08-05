package org.coner.drs.test.page.real

import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import org.coner.drs.test.page.TimerConfigurationPage
import org.coner.drs.test.testfx.chooseInChoiceBox
import org.coner.drs.ui.TimerConfigurationConverter
import org.testfx.api.FxRobot
import org.testfx.matcher.control.LabeledMatchers
import tornadofx.*
import java.nio.file.Path
import kotlin.reflect.KClass

class RealTimerConfigurationPage(val robot: FxRobot) : TimerConfigurationPage {

    private val converter = TimerConfigurationConverter()

    override fun root(): Form = robot.lookup("#timer-configuration").query()

    override fun typeChoiceBox(): ChoiceBox<KClass<*>> = robot.from(root()).lookup("#type").query()

    override fun chooseType(timerConfiguration: KClass<*>) {
        val match = converter.toString(timerConfiguration)
        robot.chooseInChoiceBox(typeChoiceBox(), LabeledMatchers.hasText(match))
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

package org.coner.drs.test.page.real

import javafx.scene.Node
import javafx.scene.control.Button
import org.coner.drs.test.page.TimerConfigurationPage
import org.coner.drs.test.page.TimerPage
import org.hamcrest.CoreMatchers
import org.testfx.api.FxRobot
import org.testfx.matcher.base.GeneralMatchers
import org.testfx.matcher.base.NodeMatchers
import org.testfx.matcher.control.ButtonMatchers
import org.testfx.matcher.control.ComboBoxMatchers
import org.testfx.matcher.control.LabeledMatchers
import org.testfx.matcher.control.TextMatchers
import tornadofx.*

class RealTimerPage(val robot: FxRobot) : TimerPage {
    override fun root() = robot.lookup("#timer").query() as Form

    override fun configureButton(): Button = robot.from(root())
            .lookup("#configure-button")
            .queryButton()

    override fun pressConfigure() {
        robot.clickOn(configureButton())
    }

    override fun startButton(): Button {
        return robot.from(root())
                .lookup("#operation-button")
                .match(LabeledMatchers.hasText("Start"))
                .query()
    }

    override fun stopButton(): Button {
        return robot.from(root())
                .lookup("#operation-button")
                .match(LabeledMatchers.hasText("Stop"))
                .query()
    }

    override fun pressStart() {
        robot.clickOn(startButton())
    }

    override fun pressStop() {
        robot.clickOn(stopButton())
    }

    override fun toTimerConfigurationPage(): TimerConfigurationPage {
        return RealTimerConfigurationPage(robot)
    }
}

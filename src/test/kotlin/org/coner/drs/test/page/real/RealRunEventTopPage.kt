package org.coner.drs.test.page.real

import javafx.scene.control.Label
import javafx.scene.layout.HBox
import org.coner.drs.test.page.RunEventTopPage
import org.testfx.api.FxRobot

open class RealRunEventTopPage(protected val robot: FxRobot) : RunEventTopPage {

    override fun root() = robot.lookup("#run-event-top-view").query<HBox>()

    override fun eventName() = robot.from(root()).lookup("#event-name").query<Label>()
}
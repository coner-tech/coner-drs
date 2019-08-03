package org.coner.drs.test.page.real

import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyCodeCombination
import org.coner.drs.domain.entity.Run
import org.coner.drs.test.page.AddNextDriverPage
import org.coner.drs.test.page.RunEventPage
import org.coner.drs.test.page.RunEventRightDrawerPage
import org.coner.drs.test.page.RunEventTablePage
import org.testfx.api.FxRobot
import tornadofx.*

open class RealRunEventPage(protected val robot: FxRobot) : RunEventPage {

    override fun root() = robot.lookup("#run-event")
            .query() as TitledPane

    override fun toAddNextDriverPage(): AddNextDriverPage {
        return RealAddNextDriverPage(robot)
    }

    override fun toTablePage(): RunEventTablePage {
        return RealRunEventTablePage(robot)
    }

    override fun toRightDrawerPage(): RunEventRightDrawerPage {
        return RealRunEventRightDrawerPage(robot)
    }
}
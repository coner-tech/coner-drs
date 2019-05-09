package org.coner.drs.test.page.fast

import org.coner.drs.domain.entity.Event
import org.coner.drs.test.page.real.RealChooseEventPage
import org.testfx.api.FxRobot

class FastChooseEventPage(robot: FxRobot) : RealChooseEventPage(robot) {

    override fun selectEvent(matcher: (Event) -> Boolean) {
        robot.interact {
            val event = eventsTable().items.first(matcher)
            eventsTable().selectionModel.select(event)
        }
    }

    override fun clickStartButton() {
        robot.interact { startButton().fire() }
    }
}
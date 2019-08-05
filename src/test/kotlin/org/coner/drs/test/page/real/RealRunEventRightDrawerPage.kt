package org.coner.drs.test.page.real

import org.coner.drs.test.page.RunEventRightDrawerPage
import org.coner.drs.test.page.TimerPage
import org.testfx.api.FxRobot
import tornadofx.*

class RealRunEventRightDrawerPage(val robot: FxRobot) : RunEventRightDrawerPage {

    override fun root(): Drawer = robot.lookup("#run-event-right-drawer").query()

    override fun timerDrawerItem(): DrawerItem = root().items.single { it.id == "timer-item" }

    override fun expandTimer() {
        val item = timerDrawerItem()
        check(!item.expanded) { "Timer already expanded" }
        FX.runAndWait { item.expanded = true }
    }

    override fun collapseTimer() {
        val item = timerDrawerItem()
        check(item.expanded) { "Timer already collapsed" }
        FX.runAndWait { item.expanded = false }
    }

    override fun toTimerPage(): TimerPage {
        return RealTimerPage(robot)
    }

}

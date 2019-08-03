package org.coner.drs.test.page.fast

import org.coner.drs.test.page.AddNextDriverPage
import org.coner.drs.test.page.RunEventRightDrawerPage
import org.coner.drs.test.page.RunEventTablePage
import org.coner.drs.test.page.real.RealRunEventPage
import org.testfx.api.FxRobot

class FastRunEventPage(robot: FxRobot) : RealRunEventPage(robot) {
    override fun toAddNextDriverPage(): AddNextDriverPage {
        return FastAddNextDriverPage(robot)
    }

    override fun toTablePage(): RunEventTablePage {
        return FastRunEventTablePage(robot)
    }

    override fun toRightDrawerPage(): RunEventRightDrawerPage {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
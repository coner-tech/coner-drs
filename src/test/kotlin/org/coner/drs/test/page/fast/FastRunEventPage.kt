package org.coner.drs.test.page.fast

import org.coner.drs.test.page.AddNextDriverPage
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
}
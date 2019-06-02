package org.coner.drs.test.page.fast

import org.coner.drs.test.page.real.RealRunEventTablePage
import org.testfx.api.FxRobot

class FastRunEventTablePage(robot: FxRobot) : RealRunEventTablePage(robot) {

    override fun selectSequence(sequence: Int) {
        runsTable().run {
            selectionModel.select(items.first { it.sequence == sequence })
        }
    }
}
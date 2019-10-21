package org.coner.drs.test.page.fast

import javafx.scene.input.KeyCode
import org.coner.drs.test.page.AlterDriverSequencePage
import org.coner.drs.test.page.real.RealRunEventTablePage
import org.testfx.api.FxRobot

class FastRunEventTablePage(private val robot: FxRobot) : RealRunEventTablePage(robot) {

    override fun selectSequence(sequence: Int) {
        runsTable().run {
            selectionModel.select(items.first { it.sequence == sequence })
        }
    }
}
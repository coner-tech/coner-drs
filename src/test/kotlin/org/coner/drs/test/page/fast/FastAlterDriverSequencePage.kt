package org.coner.drs.test.page.fast

import org.coner.drs.test.page.AlterDriverSequencePage
import org.coner.drs.test.page.SpecifyDriverSequenceAlterationPage
import org.coner.drs.test.page.real.RealAlterDriverSequencePage
import org.testfx.api.FxRobot

class FastAlterDriverSequencePage(robot: FxRobot) : RealAlterDriverSequencePage(robot) {

    override fun clickOkButton() {
        okButton().fire()
    }

    override fun clickCancelButton() {
        cancelButton().fire()
    }

    override fun toSpecifyDriverSequenceAlterationPage(): SpecifyDriverSequenceAlterationPage {
        return FastSpecifyDriverSequenceAlterationPage(robot)
    }
}
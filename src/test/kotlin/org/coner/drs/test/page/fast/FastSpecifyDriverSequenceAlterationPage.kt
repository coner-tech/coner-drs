package org.coner.drs.test.page.fast

import org.coner.drs.domain.entity.Registration
import org.coner.drs.test.page.real.RealAddNextDriverPage
import org.coner.drs.test.page.real.RealSpecifyDriverSequenceAlterationPage
import org.testfx.api.FxRobot

class FastSpecifyDriverSequenceAlterationPage(robot: FxRobot) : RealSpecifyDriverSequenceAlterationPage(robot) {

    override fun focusNumbersField() {
        robot.interact {
            numbersField().requestFocus()
        }
    }

    override fun writeInNumbersField(s: String) {
        robot.interact {
            numbersField().text = s
        }
    }

    override fun selectRegistration(registration: Registration) {
        robot.interact {
            registrationsListView().selectionModel.select(registration)
        }
    }

}
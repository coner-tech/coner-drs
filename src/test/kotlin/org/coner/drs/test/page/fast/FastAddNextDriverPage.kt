package org.coner.drs.test.page.fast

import org.coner.drs.domain.entity.Registration
import org.coner.drs.test.page.real.RealAddNextDriverPage
import org.testfx.api.FxRobot

class FastAddNextDriverPage(robot: FxRobot) : RealAddNextDriverPage(robot) {

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

    override fun focusRegistrationsListView() {
        robot.interact {
            registrationsListView().requestFocus()
        }
    }

    override fun selectRegistration(registration: Registration) {
        robot.interact {
            registrationsListView().selectionModel.select(registration)
        }
    }

    override fun doAddSelectedRegistration() {
        robot.interact {
            addButton().fire()
        }
    }

    override fun doAddForceExactNumbers() {
        super.doAddForceExactNumbers()
        robot.interact {
            addForceExactNumbersItem().fire()
        }
    }
}
package org.coner.drs.ui.runevent

import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RegistrationModel
import tornadofx.*
class RegistrationCellFragment : ListCellFragment<Registration>() {

    val registration = RegistrationModel().bindTo(this)

    override val root = vbox {
        label(registration.numbers) {
            style {
                fontSize = 24.pt
            }
        }
        label(registration.name)
        label(registration.carModel)
        label(registration.carColor)
    }
}
package org.coner.drs.ui.runevent

import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RegistrationModel
import tornadofx.*
class RegistrationCellFragment : ListCellFragment<Registration>() {

    val registration = RegistrationModel().bindTo(this)

    override val root = vbox {
        prefWidth = 0.0
        label(registration.numbers) {
            tooltip {
                textProperty().bind(registration.numbers)
            }
            style {
                fontSize = 24.pt
            }
        }
        label(registration.name) {
            tooltip {
                textProperty().bind(registration.name)
            }
        }
        label(registration.carModel) {
            tooltip {
                textProperty().bind(registration.carModel)
            }
        }
        label(registration.carColor) {
            tooltip {
                textProperty().bind(registration.carColor)
            }
        }
    }


}
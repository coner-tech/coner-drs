package org.coner.drs.domain.model

import org.coner.drs.domain.entity.Run
import tornadofx.*

class NextDriverModel : ItemViewModel<Run>() {
    val event = bind(Run::eventProperty)
    val sequence = bind(Run::sequenceProperty)
    val registration = bind(Run::registrationProperty)
}
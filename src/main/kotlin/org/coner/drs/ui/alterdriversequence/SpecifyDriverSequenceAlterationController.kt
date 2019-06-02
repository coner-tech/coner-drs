package org.coner.drs.ui.alterdriversequence

import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.domain.service.RunService
import tornadofx.*

class SpecifyDriverSequenceAlterationController : Controller() {

    private val model: SpecifyDriverSequenceAlterationModel by inject()

    init {
        subscribe<ResetEvent> { reset() }
    }

    fun reset() {
        with(find<AlterDriverSequenceModel>()) {
            model.sequence = sequence
        }
        model.numbersField = ""
        model.registration = null
        model.validate(decorateErrors = false)
    }
}

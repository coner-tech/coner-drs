package org.coner.drs.ui.alterdriversequence

import tornadofx.*

class SpecifyDriverSequenceAlterationController : Controller() {

    private val model: SpecifyDriverSequenceAlterationModel by inject()

    fun reset() {
        model.numbersField = ""
        model.validate(decorateErrors = false)
    }
}

package org.coner.drs.ui.changedriver

import org.coner.drs.domain.service.RegistrationService
import tornadofx.*

class ChangeRunDriverController : Controller() {
    override val scope = super.scope as ChangeRunDriverScope

    val registrationService: RegistrationService by inject()
    val model: ChangeRunDriverModel by inject()

    fun buildNumbersHints(): List<String> {
        return registrationService.buildNumbersFieldHints(
                numbersField = model.numbers,
                registrationHints = scope.registrationHints,
                autoCompleteOrderPreference = scope.driverAutoCompleteOrderPreference
        )
    }

    fun changeDriver() {
        val run = scope.run

    }

}

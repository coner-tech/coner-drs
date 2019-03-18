package org.coner.drs.ui.changedriver

import org.coner.drs.domain.mapper.RegistrationHintMapper
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.io.gateway.RunGateway
import tornadofx.*

class ChangeRunDriverController : Controller() {

    val model: ChangeRunDriverModel by inject()
    val runGateway: RunGateway by inject()
    val registrationService: RegistrationService by inject()

    init {
        model.numbersProperty.onChange {  findRegistrationForNumbers() }
    }

    fun findRegistrationForNumbers() {
    }

    fun buildNumbersHints(): List<String> {
        val registrations = model.registrations
        val numbers = model.numbers
        return registrationService.search(registrations, numbers)
                .map { RegistrationHintMapper.fromRegistration(it) }
                .map { RegistrationHintMapper.toNumbersFieldSuggestion(it) }
    }

    fun changeDriver() {
        val run = model.run
        run.registration = model.registrationForNumbers
        runGateway.save(run)
    }

}

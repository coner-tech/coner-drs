package org.coner.drs.ui.runevent

import org.coner.drs.domain.entity.RegistrationHint
import org.coner.drs.domain.entity.RegistrationHintMapper
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.io.gateway.RunGateway
import tornadofx.*

class AddNextDriverController : Controller() {

    val model: AddNextDriverModel by inject()
    val runEventModel: RunEventModel by inject()
    val runGateway: RunGateway by inject()
    val registrationService: RegistrationService by inject()

    init {
        model.numbersFieldProperty.onChange { findRegistrationForNumbersField() }
    }

    fun addNextDriver() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun findRegistrationForNumbersField() {
        val registrations = runEventModel.registrations
        val numbers = model.numbersField
        model.registrationsForNumbersField.setAll(registrationService.search(registrations, numbers))
    }

    fun buildNumbersFieldSuggestions(): List<String> {
        val registrations = runEventModel.registrations
        val numbers = model.numbersField
        return registrationService.search(registrations, numbers)
                .map { RegistrationHintMapper.fromRegistration(it) }
                .map { RegistrationHintMapper.toNumbersFieldSuggestion(it) }
    }

}
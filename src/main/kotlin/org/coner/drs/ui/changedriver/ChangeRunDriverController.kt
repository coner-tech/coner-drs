package org.coner.drs.ui.changedriver

import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RegistrationHintMapper
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.io.crispyfish.RegistrationMapper
import org.coner.drs.io.service.RunIoService
import tornadofx.*

class ChangeRunDriverController : Controller() {

    val model: ChangeRunDriverModel by inject()
    val runService: RunIoService by inject()
    val registrationService: RegistrationService by inject()

    init {
        model.numbersProperty.onChange {  findRegistrationForNumbers() }
    }

    fun findRegistrationForNumbers() {
        val registrationHint = try {
            model.driverAutoCompleteOrderPreference.stringConverter.fromString(model.numbers)
        } catch (t: Throwable) {
            model.registrationForNumbers = null
            return
        }
        model.registrationForNumbers = model.registrations.firstOrNull {
            it.category == registrationHint.category
                    && it.handicap == registrationHint.handicap
                    && it.number == registrationHint.number
        } ?: RegistrationHintMapper.toRegistration(registrationHint)
    }

    fun buildNumbersHints(): List<String> {
        return registrationService.buildNumbersFieldHints(
                numbersField = model.numbers,
                registrationHints = model.registrationHints,
                autoCompleteOrderPreference = model.driverAutoCompleteOrderPreference
        )
    }

    fun changeDriver() {
        val run = model.run
        run.registration = model.registrationForNumbers
        runService.save(run)
    }

}

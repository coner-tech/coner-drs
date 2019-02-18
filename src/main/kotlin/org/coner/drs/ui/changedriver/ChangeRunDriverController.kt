package org.coner.drs.ui.changedriver

import org.coner.drs.domain.entity.RegistrationHint
import org.coner.drs.domain.entity.RegistrationHintMapper
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
        val registrationHint = try {
            RegistrationHint("", "", "") // TODO
        } catch (t: Throwable) {
            model.registrationForNumbers = null
            return
        }
        model.registrationForNumbers = model.registrations.singleOrNull {
            it.category == registrationHint.category
                    && it.handicap == registrationHint.handicap
                    && it.number == registrationHint.number
        }
    }

    fun buildNumbersHints(): List<String> {
        return registrationService.buildNumbersFieldHints(
                numbersField = model.numbers,
                registrationHints = model.registrationHints
        )
    }

    fun changeDriver() {
        val run = model.run
        run.registration = model.registrationForNumbers
        runGateway.save(run)
    }

}

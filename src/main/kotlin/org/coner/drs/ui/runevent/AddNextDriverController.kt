package org.coner.drs.ui.runevent

import org.coner.drs.domain.service.RunService
import tornadofx.*

class AddNextDriverController : Controller() {
    val model: AddNextDriverModel by inject()
    val runService: RunService by inject()
    val runEventModel: RunEventModel by inject()

    fun addNextDriverFromRegistrationListSelection() {
        runService.addNextDriver(runEventModel.event, model.registrationListSelection)
    }

    fun addNextDriverForceExactNumbers() {
        if (!model.numbersFieldContainsNumbersTokens) return // TODO: guidance
        val registration = model.numbersFieldArbitraryRegistration
        runService.addNextDriver(runEventModel.event, registration)
    }


}

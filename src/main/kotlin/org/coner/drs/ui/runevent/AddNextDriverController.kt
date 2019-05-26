package org.coner.drs.ui.runevent

import com.github.thomasnield.rxkotlinfx.observeOnFx
import javafx.collections.ListChangeListener
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.domain.service.RunService
import tornadofx.*

class AddNextDriverController : Controller() {
    val model: AddNextDriverModel by inject()
    val runService: RunService by inject()
    val runEventModel: RunEventModel by inject()
    val registrationService: RegistrationService by inject()

    fun init() {
        model.registrationList.onChange { updateRegistrationListAutoSelection(it) }
    }

    fun addNextDriverFromRegistrationListSelection() {
        runService.addNextDriver(runEventModel.event, model.registrationListSelection).subscribe()
    }

    fun addNextDriverForceExactNumbers() {
        if (!model.numbersFieldContainsNumbersTokens) return // TODO: guidance
        val registration = model.numbersFieldArbitraryRegistration
        runService.addNextDriver(runEventModel.event, registration).subscribe()
    }

    fun updateRegistrationListAutoSelection(registrationListChange: ListChangeListener.Change<out Registration>) {
        model.registrationListAutoSelectionCandidate = registrationService.findAutoSelectionCandidate(
                registrationListChange,
                model.numbersField
        )
    }


}

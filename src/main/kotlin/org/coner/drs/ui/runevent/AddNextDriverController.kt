package org.coner.drs.ui.runevent

import javafx.collections.ListChangeListener
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.domain.service.RunService
import tornadofx.*

class AddNextDriverController : Controller() {
    val model: AddNextDriverModel by inject()
    val view: AddNextDriverView by inject()
    val runService: RunService by inject()
    val runEventModel: RunEventModel by inject()
    val registrationService: RegistrationService by inject()

    init {
        model.registrationList.onChange { updateRegistrationListAutoSelection(it) }
    }

    fun addNextDriverFromRegistrationListSelection() {
        runService.addNextDriver(runEventModel.event, model.registrationListSelection).subscribe()
        reset()
    }

    fun addNextDriverForceExactNumbers() {
        if (!model.numbersFieldContainsNumbersTokens) return // TODO: guidance
        val registration = model.numbersFieldArbitraryRegistration
        runService.addNextDriver(runEventModel.event, registration).subscribe()
        reset()
    }

    fun reset() {
        model.numbersField = ""
        view.numbersField.requestFocus()
    }

    fun updateRegistrationListAutoSelection(registrationListChange: ListChangeListener.Change<out Registration>) {
        while (registrationListChange.next()) {
            model.registrationListAutoSelectionCandidate = registrationService.findAutoSelectionCandidate(
                    registrationListChange.list,
                    model.numbersField
            )
        }
    }

    val locateRunEventTable = { find<RunEventTableView>().table }

}

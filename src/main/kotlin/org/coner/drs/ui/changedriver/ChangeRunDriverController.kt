package org.coner.drs.ui.changedriver

import javafx.collections.ListChangeListener
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.domain.service.RunService
import tornadofx.*

class ChangeRunDriverController : Controller() {

    val model: ChangeRunDriverModel by inject()
    val registrationService: RegistrationService by inject()
    val runService: RunService by inject()

    fun init() {
        model.registrationList.onChange {  updateRegistrationListAutoSelection(it) }
    }

    fun updateRegistrationListAutoSelection(registrationListChange: ListChangeListener.Change<out Registration>) {
        model.registrationListAutoSelectionCandidate = registrationService.findAutoSelectionCandidate(
                registrationListChange,
                model.numbersField
        )
    }

    fun changeDriverFromRegistrationListSelection() {
        runService.changeDriver(model.run, model.registrationListSelection)
    }

    fun changeDriverFromExactNumbers() {
        runService.changeDriver(model.run, model.numbersFieldArbitraryRegistration)
    }

    fun clearDriver() {
        runService.changeDriver(model.run, null)
    }

}

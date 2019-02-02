package org.coner.drs.ui.changedriver

import javafx.collections.ObservableList
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.domain.entity.DriverAutoCompleteOrderPreference
import org.coner.drs.domain.entity.RegistrationHint
import org.coner.drs.domain.entity.Run
import tornadofx.*

class ChangeRunDriverScope(
        registrationService: RegistrationService,
        val run: Run,
        val registrations: ObservableList<Registration>,
        val driverAutoCompleteOrderPreference: DriverAutoCompleteOrderPreference,
        val registrationHints: Set<RegistrationHint>
) : Scope(registrationService)
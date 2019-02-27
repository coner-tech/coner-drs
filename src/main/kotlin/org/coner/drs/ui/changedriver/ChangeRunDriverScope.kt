package org.coner.drs.ui.changedriver

import javafx.collections.ObservableList
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.domain.entity.RegistrationHint
import org.coner.drs.domain.entity.Run
import org.coner.drs.io.gateway.RunGateway
import tornadofx.*

class ChangeRunDriverScope(
        runEventScope: Scope,
        run: Run,
        registrations: ObservableList<Registration>
) : Scope(
        find<RunGateway>(runEventScope),
        find<RegistrationService>(runEventScope)
) {
    init {
        set(ChangeRunDriverModel(
                run = run,
                registrations = registrations
        ))
    }
}
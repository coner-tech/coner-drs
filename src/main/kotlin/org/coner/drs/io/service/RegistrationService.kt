package org.coner.drs.io.service

import org.coner.drs.Event
import org.coner.drs.Registration
import org.coner.drs.io.crispyfish.RegistrationMapper
import org.coner.drs.io.crispyfish.buildEventControlFile
import tornadofx.*

class RegistrationService : Controller() {

    fun list(event: Event): List<Registration> {
        return event.buildEventControlFile()
                .queryRegistrations()
                .map { RegistrationMapper.toUiEntity(it) }
    }
}
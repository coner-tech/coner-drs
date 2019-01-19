package org.coner.drs.io.service

import io.reactivex.Single
import org.coner.drs.Event
import org.coner.drs.Registration
import org.coner.drs.io.crispyfish.RegistrationMapper
import org.coner.drs.io.crispyfish.buildEventControlFile
import tornadofx.*

class RegistrationService : Controller() {

    fun list(event: Event): Single<List<Registration>> = Single.fromCallable {
        event.buildEventControlFile()
                .queryRegistrations()
                .map { RegistrationMapper.toUiEntity(it) }
    }
}
package org.coner.drs.io.gateway

import de.helmbold.rxfilewatcher.PathObservables
import io.reactivex.Observable
import io.reactivex.Single
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.mapper.RegistrationMapper
import org.coner.drs.io.crispyfish.buildEventControlFile
import tornadofx.*
import java.nio.file.Path

class RegistrationGateway : Controller() {

    fun list(event: Event): Single<List<Registration>> = Single.fromCallable {
        event.buildEventControlFile()
                .queryRegistrations()
                .map { RegistrationMapper.toUiEntity(it) }
    }

    fun watchList(event: Event): Observable<List<Registration>> {
        val eventControlFile = event.buildEventControlFile()
        val registrationFileName = eventControlFile.registrationFile().file.name
        return PathObservables.watchNonRecursive(eventControlFile.file.parentFile.toPath())
                .filter {
                    val watchedEventFileName = (it.context() as Path).toFile().name
                    watchedEventFileName == registrationFileName
                }
                .map { list(event).blockingGet() }
    }
}
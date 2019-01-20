package org.coner.drs.io.service

import de.helmbold.rxfilewatcher.PathObservables
import io.reactivex.Observable
import io.reactivex.Single
import org.coner.drs.Event
import org.coner.drs.Registration
import org.coner.drs.io.crispyfish.RegistrationMapper
import org.coner.drs.io.crispyfish.buildEventControlFile
import org.coner.drs.io.db.EntityWatchEvent
import tornadofx.*
import java.nio.file.Path
import java.nio.file.WatchEvent

class RegistrationService : Controller() {

    fun list(event: Event): Single<List<Registration>> = Single.fromCallable {
        event.buildEventControlFile()
                .queryRegistrations()
                .map { RegistrationMapper.toUiEntity(it) }
    }

    fun watchList(event: Event): Observable<List<Registration>> {
        return PathObservables.watchNonRecursive(
                event.buildEventControlFile().file.parentFile.toPath()
        )
                .filter {
                    val watchedEventFileName = (it.context() as Path).toFile().name
                    val registrationFileName = event.buildEventControlFile().registrationFile().file.name
                    watchedEventFileName == registrationFileName
                }
                .map { list(event).blockingGet() }
    }
}
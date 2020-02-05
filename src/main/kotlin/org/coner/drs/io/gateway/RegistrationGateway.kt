/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.coner.drs.io.gateway

import io.reactivex.Observable
import io.reactivex.Single
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.mapper.RegistrationMapper
import org.coner.drs.io.crispyfish.buildEventControlFile
import org.coner.snoozle.util.watch
import tornadofx.*
import java.nio.file.Path
import java.nio.file.WatchEvent

class RegistrationGateway : Controller() {

    fun list(event: Event): Single<List<Registration>> = Single.fromCallable {
        event.buildEventControlFile()
                .queryRegistrations()
                .map { RegistrationMapper.toUiEntity(it) }
    }

    fun watchList(event: Event): Observable<List<Registration>> {
        val eventControlFile = event.buildEventControlFile()
        val registrationFileName = eventControlFile.registrationFile().file.name
        fun WatchEvent<*>.fileName(): String = (context() as Path).toFile().name
        return eventControlFile.file.parentFile.toPath().watch(recursive = false)
                .filter { watchEvent -> watchEvent.fileName() == registrationFileName }
                .map { list(event).blockingGet() }
    }
}
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

package org.coner.drs.test.fixture

import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.io.gateway.EventGateway
import org.coner.drs.io.gateway.RegistrationGateway
import org.coner.drs.io.gateway.RunGateway
import tornadofx.*
import java.util.*

object FixtureUtil {

    fun loadRunEvent(app: App, fixture: TestEventFixture.Instance, eventId: UUID): RunEvent {
        // TODO: evaluate need to open io
        val eventGateway: EventGateway = find(app.scope)
        val event = eventGateway.list().single { it.id == eventId }.let {
            eventGateway.asRunEvent(it)!!
        }
        val runGateway: RunGateway = find(app.scope)
        event.runs.addAll(runGateway.list(event).blockingGet())
        val registrationGateway: RegistrationGateway = find(app.scope)
        runGateway.hydrateWithRegistrationMetadata(event.runs, registrationGateway.list(event).blockingGet())
        return event
    }
}
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

package org.coner.drs.test.fixture.domain.entity

import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RunEvent
import java.time.LocalDate
import java.util.*

object RunEvents {

    fun basic() = RunEvent(
            id = UUID.randomUUID(),
            date = LocalDate.of(2019, 3, 25),
            name = "basic",
            crispyFishMetadata = Event.CrispyFishMetadata()
    ).apply {
        registrations.addAll(
                Registration(
                        name = "Carlton Whitehead",
                        number = "8",
                        handicap = "STR",
                        category = "",
                        carModel = "2002 Honda S2000",
                        carColor = "Silver"
                ),
                Registration(
                        name = "Austin Culbertson",
                        number = "99",
                        handicap = "HS",
                        category = "",
                        carModel = "2014 Ford Fiesta ST",
                        carColor = "Green"
                ),
                Registration(
                        name = "Brice Johnson",
                        number = "15",
                        handicap = "CS",
                        category = "",
                        carModel = "2012 Mazda MX-5",
                        carColor = "Red"
                )
        )
    }
}
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
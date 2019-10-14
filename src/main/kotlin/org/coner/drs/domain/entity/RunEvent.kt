package org.coner.drs.domain.entity

import com.github.thomasnield.rxkotlinfx.observeOnFx
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.transformation.SortedList
import org.coner.drs.domain.service.RunService
import org.coner.drs.io.db.WatchedEntity
import java.time.LocalDate
import java.util.*
import tornadofx.*

class RunEvent(
        id: UUID,
        date: LocalDate,
        name: String,
        crispyFishMetadata: Event.CrispyFishMetadata,
        private val service: RunService = find()
): Event(
        id = id,
        date = date,
        name = name,
        crispyFishMetadata = crispyFishMetadata
) {

    val registrations = observableListOf<Registration>()
    val runs = observableListOf<Run> { arrayOf(it.sequenceProperty, it.registrationProperty, it.rawTimeProperty) }
    val runsBySequence = SortedList(runs, compareBy(Run::sequence))

    val runForNextDriverBinding = objectBinding(runsBySequence) {
        service.findRunForNextDriver(this@RunEvent)
    }
    val runForNextDriverProperty = SimpleObjectProperty<Run>(this, "runForNextDriver").apply {
        bind(runForNextDriverBinding)
    }
    val runForNextDriver by runForNextDriverProperty

    val runForNextTimeBinding = objectBinding(runsBySequence) {
        service.findRunForNextTime(this@RunEvent).blockingGet()
    }
    val runForNextTimeProperty = SimpleObjectProperty<Run>(this, "runForNextTime").apply {
        bind(runForNextTimeBinding)
    }
    val runForNextTime: Run by runForNextTimeProperty

}
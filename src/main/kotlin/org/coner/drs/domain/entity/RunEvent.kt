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
    val runs = observableListOf<Run>()
    val runsBySequence = SortedList(runs, compareBy(Run::sequence))

    val runForNextDriverBinding = objectBinding(runsBySequence) {
        val indexOfLastRunWithDriver = runsBySequence.indexOfLast { it.registration != null }
        if (indexOfLastRunWithDriver >= 0) {
            if (indexOfLastRunWithDriver in 0 until runsBySequence.lastIndex) {
                runsBySequence[indexOfLastRunWithDriver + 1]
            } else {
                Run(
                        event = this@RunEvent,
                        sequence = runsBySequence[indexOfLastRunWithDriver].sequence + 1
                )
            }
        } else {
            Run(
                    event = this@RunEvent,
                    sequence = 1
            )
        }
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
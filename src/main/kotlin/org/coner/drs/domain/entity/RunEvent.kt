package org.coner.drs.domain.entity

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.transformation.SortedList
import org.coner.drs.io.db.WatchedEntity
import java.time.LocalDate
import java.util.*
import tornadofx.*

class RunEvent(
        id: UUID,
        date: LocalDate,
        name: String,
        crispyFishMetadata: Event.CrispyFishMetadata
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
        val indexOfLastRunWithTime = runsBySequence.indexOfLast { it.rawTime != null }
        if (indexOfLastRunWithTime >= 0) {
            if (indexOfLastRunWithTime in 0 until runsBySequence.lastIndex) {
                runsBySequence[indexOfLastRunWithTime + 1]
            } else {
                // Consider error reporting for this scenario.
                // There has possibly been a finish trip without a run ready with a driver awaiting a time.
                // Perhaps a car managed to stage and launch without being noticed by Timing workers.
                // Recommend to hold start while resolving situation.
                Run(
                        event = this@RunEvent,
                        sequence = runsBySequence[indexOfLastRunWithTime].sequence + 1
                )
            }
        } else {
            Run(
                    event = this@RunEvent,
                    sequence = 1
            )
        }
    }
    val runForNextTimeProperty = SimpleObjectProperty<Run>(this, "runForNextTime").apply {
        bind(runForNextTimeBinding)
    }
    val runForNextTime: Run by runForNextTimeProperty

}
package org.coner.drs.ui.alterdriversequence

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.*

class PreviewAlteredDriverSequenceResult(
        runs: ObservableList<Run>
) {
    val runsProperty = SimpleListProperty<Run>(runs)
    var runs by runsProperty

    class Run(run: org.coner.drs.domain.entity.Run, status: Status) {
        val sequence = SimpleIntegerProperty(run.sequence)
        val status = SimpleObjectProperty(status)
        val numbers = SimpleStringProperty(run.registrationNumbers)
        val name = SimpleStringProperty(run.registrationDriverName)
        val carModel = SimpleStringProperty(run.registrationCarModel)
        val carColor = SimpleStringProperty(run.registrationCarColor)
        val rawTime = SimpleObjectProperty(run.rawTime)
        val compositePenalty = SimpleObjectProperty(run.compositePenalty)
    }

    enum class Status {
        SAME,
        INSERTED,
        SHIFTED
    }


}
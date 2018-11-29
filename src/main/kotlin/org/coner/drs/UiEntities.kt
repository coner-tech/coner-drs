package org.coner.drs

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import tornadofx.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import tornadofx.getValue
import tornadofx.setValue

class Event(
        id: UUID = UUID.randomUUID(),
        date: LocalDate,
        name: String
) {
    val idProperty = SimpleObjectProperty<UUID>(this, "id", id)
    var id by idProperty

    val dateProperty = SimpleObjectProperty<LocalDate>(this, "date", date)
    var date by dateProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val categories = FXCollections.observableSet<String>()
    val handicaps = FXCollections.observableSet<String>()
    val numbers = FXCollections.observableSet<String>()
}

class Run(
        id: UUID = UUID.randomUUID(),
        event: Event
) {
    val idProperty = SimpleObjectProperty<UUID>(this, "id", id)
    var id by idProperty

    val eventProperty = SimpleObjectProperty<Event>(this, "event", event)
    var event by eventProperty

    val sequenceProperty = SimpleIntegerProperty(this, "sequence")
    var sequence by sequenceProperty

    val categoryProperty = SimpleStringProperty(this, "category", "")
    var category by categoryProperty

    val handicapProperty = SimpleStringProperty(this, "handicap", "")
    var handicap by handicapProperty

    val numberProperty = SimpleStringProperty(this, "number", "")
    var number by numberProperty

    val rawTimeProperty = SimpleObjectProperty<BigDecimal>(this, "rawTime")
    var rawTime by rawTimeProperty

    val conesProperty = SimpleIntegerProperty(this, "cones", 0)
    var cones by conesProperty

    val didNotFinishProperty = SimpleBooleanProperty(this, "didNotFinish", false)
    var didNotFinish by didNotFinishProperty

    val disqualifiedProperty = SimpleBooleanProperty(this, "disqualified", false)
    var disqualified by disqualifiedProperty

    val rerunProperty = SimpleBooleanProperty(this, "rerun", false)
    var rerun by rerunProperty

}

class RunModel : ItemViewModel<Run>() {
    val id = bind(Run::idProperty)
    val event = bind(Run::eventProperty)
    val sequence = bind(Run::sequenceProperty)
    val category = bind(Run::categoryProperty)
    val handicap = bind(Run::handicapProperty)
    val number = bind(Run::numberProperty)
    val rawTime = bind(Run::rawTimeProperty)
    val cones = bind(Run::conesProperty)
    val didNotFinish = bind(Run::didNotFinishProperty)
    val disqualified = bind(Run::disqualifiedProperty)
    val rerun = bind(Run::rerunProperty)
}

package org.coner.rs

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

    var categories = observableList<String>()
    var handicaps = observableList<String>()
    var numbers = observableList<String>()
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

    val categoryProperty = SimpleStringProperty(this, "category")
    var category by categoryProperty

    val handicapProperty = SimpleStringProperty(this, "handicap")
    var handicap by handicapProperty

    val numberProperty = SimpleStringProperty(this, "number")
    var number by numberProperty

    val rawTimeProperty = SimpleObjectProperty<BigDecimal>(this, "rawTime")
    var rawTime by rawTimeProperty

    val conesProperty = SimpleIntegerProperty(this, "cones")
    var cones by conesProperty

    val didNotFinishProperty = SimpleBooleanProperty(this, "didNotFinish")
    var didNotFinish by didNotFinishProperty

    val disqualifiedProperty = SimpleBooleanProperty(this, "disqualified")
    var disqualified by disqualifiedProperty

    val rerunProperty = SimpleBooleanProperty(this, "rerun")
    var rerun by rerunProperty

}
package org.coner.rs

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

class Category(
        id: UUID = UUID.randomUUID(),
        name: String
) {
    val idProperty = SimpleObjectProperty<UUID>(this, "id", id)
    var id by idProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

}

class Handicap(
        id: UUID = UUID.randomUUID(),
        name: String
) {
    val idProperty = SimpleObjectProperty<UUID>(this, "id", id)
    var id by idProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

}

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

    var registrations = observableList<Registration>()
    var categories = observableList<Category>()
    var handicaps = observableList<Handicap>()

}

class Registration(
        id: UUID = UUID.randomUUID(),
        event: Event
) {
    val idProperty = SimpleObjectProperty<UUID>(this, "id", id)
    var id by idProperty

    val eventProperty = SimpleObjectProperty<Event>(this, "event", event)
    var event by eventProperty

    val categoryProperty = SimpleObjectProperty<Category>(this, "category")
    var category by categoryProperty

    val handicapProperty = SimpleObjectProperty<Handicap>(this, "handicap")
    var handicap by handicapProperty

    val numberProperty = SimpleStringProperty(this, "number")
    var number by numberProperty

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

    val registrationProperty = SimpleObjectProperty<Registration>(this, "registration")
    var registration by registrationProperty

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
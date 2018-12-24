package org.coner.drs

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import org.coner.crispyfish.filetype.classdefinition.ClassDefinitionFile
import org.coner.crispyfish.filetype.ecf.EventControlFile
import tornadofx.*
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

class Event(
        id: UUID = UUID.randomUUID(),
        date: LocalDate,
        name: String,
        classDefinitionFile: ClassDefinitionFile,
        eventControlFile: EventControlFile
) {
    val idProperty = SimpleObjectProperty<UUID>(this, "id", id)
    var id by idProperty

    val dateProperty = SimpleObjectProperty<LocalDate>(this, "date", date)
    var date by dateProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val classDefinitionFileProperty = SimpleObjectProperty<ClassDefinitionFile>(
            this,
            "classDefinitionFile",
            classDefinitionFile
    )
    var classDefinitionFile by classDefinitionFileProperty

    val eventControlFileProperty = SimpleObjectProperty<EventControlFile>(
            this,
            "eventControlFile",
            eventControlFile
    )
    var eventControlFile by eventControlFileProperty


    val categories = FXCollections.observableSet<String>()
    val handicaps = FXCollections.observableSet<String>()
    val numbers = FXCollections.observableSet<String>()
}

class Run(
        id: UUID = UUID.randomUUID(),
        event: Event,
        sequence: Int = 0,
        category: String = "",
        handicap: String = "",
        number: String = "",
        rawTime: BigDecimal? = null,
        cones: Int = 0,
        didNotFinish: Boolean = false,
        disqualified: Boolean = false,
        rerun: Boolean = false
) {
    val idProperty = SimpleObjectProperty<UUID>(this, "id", id)
    var id by idProperty

    val eventProperty = SimpleObjectProperty<Event>(this, "event", event)
    var event by eventProperty

    val sequenceProperty = SimpleIntegerProperty(this, "sequence", sequence)
    var sequence by sequenceProperty

    val categoryProperty = SimpleStringProperty(this, "category", category)
    var category by categoryProperty

    val handicapProperty = SimpleStringProperty(this, "handicap", handicap)
    var handicap by handicapProperty

    val numberProperty = SimpleStringProperty(this, "number", number)
    var number by numberProperty

    val rawTimeProperty = SimpleObjectProperty<BigDecimal>(this, "rawTime", rawTime)
    var rawTime by rawTimeProperty

    val conesProperty = SimpleIntegerProperty(this, "cones", cones)
    var cones by conesProperty

    val didNotFinishProperty = SimpleBooleanProperty(this, "didNotFinish", didNotFinish)
    var didNotFinish by didNotFinishProperty

    val disqualifiedProperty = SimpleBooleanProperty(this, "disqualified", disqualified)
    var disqualified by disqualifiedProperty

    val rerunProperty = SimpleBooleanProperty(this, "rerun", rerun)
    var rerun by rerunProperty

}

class NextDriverModel : ItemViewModel<Run>() {
    val event = bind(Run::eventProperty)
    val sequence = bind(Run::sequenceProperty)
    val category = bind(Run::categoryProperty)
    val handicap = bind(Run::handicapProperty)
    val number = bind(Run::numberProperty)
}

sealed class TimerConfiguration {
    data class SerialPortInput(val port: String) : TimerConfiguration() { companion object { }}
    data class FileInput(val file: File) : TimerConfiguration() { companion object { }}
}
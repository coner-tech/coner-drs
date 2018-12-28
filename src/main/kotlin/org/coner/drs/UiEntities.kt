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
import java.io.File

class Event(
        id: UUID = UUID.randomUUID(),
        date: LocalDate = LocalDate.now(),
        name: String = "",
        crispyFishMetadata: CrispyFishMetadata = CrispyFishMetadata()
) {
    val idProperty = SimpleObjectProperty<UUID>(this, "id", id)
    var id by idProperty

    val dateProperty = SimpleObjectProperty<LocalDate>(this, "date", date)
    var date by dateProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val crispyFishMetadataProperty = SimpleObjectProperty<CrispyFishMetadata>(
            this, "crispyFishMetadata", crispyFishMetadata
    )
    var crispyFishMetadata by crispyFishMetadataProperty

    val categories = FXCollections.observableSet<String>()
    val handicaps = FXCollections.observableSet<String>()
    val numbers = FXCollections.observableSet<String>()
    val registrations = FXCollections.observableSet<Registration>()

    class CrispyFishMetadata(
            classDefinitionFile: File = File(""),
            eventControlFile: File = File("")
    ) {
        val classDefinitionFileProperty = SimpleObjectProperty<File>(
                this,
                "classDefinitionFile",
                classDefinitionFile
        )
        var classDefinitionFile by classDefinitionFileProperty

        val eventControlFileProperty = SimpleObjectProperty<File>(
                this,
                "eventControlFile",
                eventControlFile
        )
        var eventControlFile by eventControlFileProperty
    }
}

class EventModel : ItemViewModel<Event>(Event()) {
    val date = bind(Event::dateProperty, autocommit = true)
    val name = bind(Event::nameProperty, autocommit = true)
}

class EventCrispyFishMetadataModel : ItemViewModel<Event.CrispyFishMetadata>(Event.CrispyFishMetadata()) {
    val eventControlFile = bind(Event.CrispyFishMetadata::eventControlFileProperty, autocommit = true)
    val classDefinitionFile = bind(Event.CrispyFishMetadata::classDefinitionFileProperty, autocommit = true)
}

class Registration(
        category: String,
        handicap: String,
        number: String,
        name: String,
        carModel: String,
        carColor: String
) {
    val categoryProperty = SimpleStringProperty(this, "category", category)
    var category by categoryProperty

    val handicapProperty = SimpleStringProperty(this, "handicap", handicap)
    var handicap by handicapProperty

    val numberProperty = SimpleStringProperty(this, "number", number)
    var number by numberProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val carModelProperty = SimpleStringProperty(this, "carModel", carModel)
    var carModel by carModelProperty

    val carColorProperty = SimpleStringProperty(this, "carColor", carColor)
    var carColor by carColorProperty

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
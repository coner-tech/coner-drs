package org.coner.drs.domain.entity

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import java.io.File
import java.time.LocalDate
import java.util.*
import tornadofx.*

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
package org.coner.drs.domain.entity

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.coner.drs.io.db.WatchedEntity
import java.io.File
import java.time.LocalDate
import java.util.*
import tornadofx.*

open class Event(
        id: UUID = UUID.randomUUID(),
        date: LocalDate = LocalDate.now(),
        name: String = "",
        crispyFishMetadata: CrispyFishMetadata = CrispyFishMetadata()
): WatchedEntity<Event> {
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

    override fun onWatchedEntityUpdate(updated: Event) {
        date = updated.date
        name = updated.name
    }
}
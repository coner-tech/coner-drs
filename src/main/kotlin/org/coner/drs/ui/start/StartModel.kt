package org.coner.drs.ui.start

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.io.File
import tornadofx.getValue
import tornadofx.setValue
import java.nio.file.Path
import java.nio.file.Paths

class StartModel : ViewModel() {
    val rawSheetDatabaseProperty = SimpleObjectProperty<Path>(
            this,
            "rawSheetDatabase",
            with(config) { if (containsKey("rawSheetDatabase")) Paths.get(string("rawSheetDatabase")) else null }
    )
    var rawSheetDatabase by rawSheetDatabaseProperty
    val crispyFishDatabaseProperty = SimpleObjectProperty<File>(
            this,
            "crispyFishDatabase",
            with(config) { if (containsKey("crispyFishDatabase")) File(string("crispyFishDatabase")) else null }
    )
    var crispyFishDatabase by crispyFishDatabaseProperty

    val subscriberProperty = SimpleBooleanProperty(
            this,
            "subscriber",
            config.boolean("subscriber", true)
    )
    var subscriber by subscriberProperty

    override fun onCommit() {
        with(config) {
            set("rawSheetDatabase" to rawSheetDatabase)
            set("crispyFishDatabase" to crispyFishDatabase.absolutePath)
            set("subscriber" to subscriber)
            save()
        }
    }
}
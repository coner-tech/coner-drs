package org.coner.drs.ui.start

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.io.File
import tornadofx.getValue
import tornadofx.setValue

class StartModel : ViewModel() {
    val rawSheetDatabaseProperty = SimpleObjectProperty<File>(
            this,
            "rawSheetDatabase",
            with(config) { if (containsKey("rawSheetDatabase")) File(string("rawSheetDatabase")) else null }
    )
    var rawSheetDatabase by rawSheetDatabaseProperty
    val crispyFishDatabaseProperty = SimpleObjectProperty<File>(
            this,
            "crispyFishDatabase",
            with(config) { if (containsKey("crispyFishDatabase")) File(string("crispyFishDatabase")) else null }
    )
    var crispyFishDatabase by crispyFishDatabaseProperty

    override fun onCommit() {
        with(config) {
            set("rawSheetDatabase" to rawSheetDatabase.absolutePath)
            set("crispyFishDatabase" to crispyFishDatabase.absolutePath)
            save()
        }
    }
}
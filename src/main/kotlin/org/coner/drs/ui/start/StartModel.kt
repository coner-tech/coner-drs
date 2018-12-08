package org.coner.drs.ui.start

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.io.File

class StartModel : ViewModel() {
    val rawSheetDatabaseProperty = SimpleObjectProperty<File>(this, "rawSheetDatabase")
    var rawSheetDatabase by rawSheetDatabaseProperty
}
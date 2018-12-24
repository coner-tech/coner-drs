package org.coner.drs.ui.start

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.io.File
import tornadofx.getValue
import tornadofx.setValue

class StartModel : ViewModel() {
    val rawSheetDatabaseProperty = SimpleObjectProperty<File>(this, "rawSheetDatabase")
    var rawSheetDatabase by rawSheetDatabaseProperty
    val crispyFishDatabaseProperty = SimpleObjectProperty<File>(this, "crispyFishDatabase")
    var crispyFishDatabase by crispyFishDatabaseProperty

}
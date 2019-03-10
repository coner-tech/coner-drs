package org.coner.drs.ui.runevent

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.coner.drs.domain.entity.Registration
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class AddNextDriverModel : ViewModel() {

    val numbersFieldProperty = SimpleStringProperty(this, "numbers", "")
    var numbersField by numbersFieldProperty

    val registrationsForNumbersField = observableList<Registration>()

    val nextRunSequenceProperty = SimpleIntegerProperty(this, "nextRunSequence")
    var nextRunSequence by nextRunSequenceProperty

}
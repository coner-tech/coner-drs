package org.coner.drs.ui.runevent

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import org.coner.drs.domain.model.NextDriverModel
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RegistrationHint
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class AddNextDriverModel : ViewModel() {

    val nextDriver: NextDriverModel by inject()
    val registrationHints = FXCollections.observableSet<RegistrationHint>()

    val numbersFieldProperty = SimpleStringProperty(this, "numbers", "")
    var numbersField by numbersFieldProperty

    val registrationHintsToRegistrations = FXCollections.observableHashMap<RegistrationHint, Registration>()
}
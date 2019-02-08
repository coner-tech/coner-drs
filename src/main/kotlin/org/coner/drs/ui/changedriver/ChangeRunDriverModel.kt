package org.coner.drs.ui.changedriver

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import org.coner.drs.domain.entity.DriverAutoCompleteOrderPreference
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RegistrationHint
import org.coner.drs.domain.entity.Run
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class ChangeRunDriverModel(
        val run: Run,
        val registrations: ObservableList<Registration>,
        val driverAutoCompleteOrderPreference: DriverAutoCompleteOrderPreference,
        val registrationHints: Set<RegistrationHint>
) : ViewModel() {

    val numbersProperty = SimpleStringProperty(this, "numbers", "")
    var numbers by numbersProperty
    val registrationForNumbersProperty = SimpleObjectProperty<Registration>(
            this,
            "registrationForNumbers",
            Registration(category = "", handicap = "", number = "")
    )
    var registrationForNumbers by registrationForNumbersProperty


}
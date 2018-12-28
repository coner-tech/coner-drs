package org.coner.drs.ui.runevent

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import org.coner.drs.NextDriverModel
import org.coner.drs.Registration
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class AddNextDriverModel : ViewModel() {

    val nextDriver: NextDriverModel by inject()
    val registrationHints = FXCollections.observableSet<RegistrationHint>()

    val numbersFieldProperty = SimpleStringProperty(this, "numbers", "")
    var numbersField by numbersFieldProperty

    val registrationForNumbersProperty = SimpleObjectProperty<Registration>(this, "registration")
    var registrationForNumbers by registrationForNumbersProperty

    val driverAutoCompleteOrderPreferenceProperty = SimpleObjectProperty<DriverAutoCompleteOrderPreference>(
            this,
            "driverAutoCompleteOrderPreference",
            DriverAutoCompleteOrderPreference.NumberCategoryHandicap
    )
    var driverAutoCompleteOrderPreference by driverAutoCompleteOrderPreferenceProperty

    val driverAutoCompleteOrderPreferences = listOf(
            DriverAutoCompleteOrderPreference.NumberCategoryHandicap,
            DriverAutoCompleteOrderPreference.CategoryHandicapNumber
    )
    val registrationHintsToRegistrations = FXCollections.observableHashMap<RegistrationHint, Registration>()
}
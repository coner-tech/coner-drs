package org.coner.drs.ui.run_event

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import org.coner.drs.NextDriverModel
import tornadofx.*

class AddNextDriverModel : ViewModel() {

    val nextDriver: NextDriverModel by inject()
    val registrationHints = FXCollections.observableSet<RegistrationHint>()

    val numbersFieldProperty = SimpleStringProperty(this, "numbers", "")
    var numbersField by numbersFieldProperty

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
}
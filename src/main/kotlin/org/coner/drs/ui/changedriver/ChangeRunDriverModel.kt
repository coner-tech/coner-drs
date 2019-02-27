package org.coner.drs.ui.changedriver

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RegistrationHint
import org.coner.drs.domain.entity.Run
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class ChangeRunDriverModel(
        val run: Run,
        val registrations: ObservableList<Registration>
) : ViewModel() {

    val numbersProperty = SimpleStringProperty(this, "numbers", "")
    var numbers by numbersProperty

    val registrationForNumbersProperty = SimpleObjectProperty<Registration>(this, "registration")
    val registrationForNumbers by registrationForNumbersProperty

    val registrationNameProperty = SimpleStringProperty(this, "registrationName").apply {
        bind(registrationForNumbersProperty.select { it.nameProperty })
    }
    val registrationName by registrationNameProperty

    val registrationCarModelProperty = SimpleStringProperty(this, "registrationCarModel").apply {
        bind(registrationForNumbersProperty.select { it.carModelProperty })
    }
    var registrationCarModel by registrationCarModelProperty

    val registrationCarColorProperty = SimpleStringProperty(this, "registrationCarColor").apply {
        bind(registrationForNumbersProperty.select { it.carColorProperty })
    }
    var registrationCarColor by registrationCarColorProperty


}
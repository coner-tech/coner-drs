package org.coner.drs.domain.entity

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class Registration(
        category: String,
        handicap: String,
        number: String,
        name: String? = null,
        carModel: String? = null,
        carColor: String? = null
) {
    val categoryProperty = SimpleStringProperty(this, "category", category)
    var category by categoryProperty

    val handicapProperty = SimpleStringProperty(this, "handicap", handicap)
    var handicap by handicapProperty

    val numberProperty = SimpleStringProperty(this, "number", number)
    var number by numberProperty

    val nameProperty = SimpleStringProperty(this, "name", name)
    var name by nameProperty

    val carModelProperty = SimpleStringProperty(this, "carModel", carModel)
    var carModel by carModelProperty

    val carColorProperty = SimpleStringProperty(this, "carColor", carColor)
    var carColor by carColorProperty

    private val numbersBinding = stringBinding(numberProperty, categoryProperty, handicapProperty) {
        arrayOf(number, category, handicap)
                .filterNot { it.isNullOrBlank() }
                .joinToString(" ")
    }
    val numbersProperty = SimpleStringProperty(this, "numbers").apply {
        bind(numbersBinding)
    }
    val numbers by numbersProperty

    fun clone(
            category: String? = null,
            handicap: String? = null,
            number: String? = null,
            name: String? = null,
            carModel: String? = null,
            carColor: String? = null
    ) = Registration(
            category = category ?: this.category,
            handicap = handicap ?: this.handicap,
            number = number ?: this.number,
            name = name ?: this.name,
            carModel = carModel ?: this.carModel,
            carColor = carColor ?: this.carColor
    )

}

class RegistrationModel : ItemViewModel<Registration>() {
    val category = bind(Registration::categoryProperty)
    val handicap = bind(Registration::handicapProperty)
    val number = bind(Registration::numberProperty)
    val name = bind(Registration::nameProperty)
    val carModel = bind(Registration::carModelProperty)
    val carColor = bind(Registration::carColorProperty)

    val numbers = bind(Registration::numbersProperty)

}
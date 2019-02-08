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
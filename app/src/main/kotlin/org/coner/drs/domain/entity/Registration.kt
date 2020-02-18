/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
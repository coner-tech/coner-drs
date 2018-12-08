package org.coner.drs.ui.run_event

import javafx.util.StringConverter

sealed class DriverAutoCompleteOrderPreference {
    abstract val text: String
    abstract val stringConverter: StringConverter<RegistrationHint>

    object NumberCategoryHandicap : DriverAutoCompleteOrderPreference() {
        override val text = "Number Category Handicap"
        override val stringConverter = object : StringConverter<RegistrationHint>() {
            override fun toString(p0: RegistrationHint) = arrayOf(
                    p0.number,
                    p0.category,
                    p0.handicap
            ).filterNot { it.isBlank() }.joinToString(" ")

            override fun fromString(p0: String): RegistrationHint {
                val split = p0.split(" ")
                return when(split.size) {
                    2 -> RegistrationHint(number = split[0], category = "", handicap = split[1])
                    3 -> RegistrationHint(number = split[0], category = split[1], handicap = split[2])
                    else -> throw IllegalArgumentException("Invalid registration hint: $p0")
                }
            }
        }
    }

    object CategoryHandicapNumber : DriverAutoCompleteOrderPreference() {
        override val text = "Category Handicap Number"
        override val stringConverter = object : StringConverter<RegistrationHint>() {
            override fun toString(p0: RegistrationHint) = arrayOf(
                    p0.category,
                    p0.handicap,
                    p0.number
            ).filterNot { it.isBlank() }.joinToString(" ")

            override fun fromString(p0: String): RegistrationHint {
                val split = p0.split(" ")
                return when (split.size) {
                    2 -> RegistrationHint(category = "", handicap = split[0], number = split[1])
                    3 -> RegistrationHint(category = split[0], handicap = split[1], number = split[2])
                    else -> throw IllegalArgumentException("Invalid registration hint: $p0")
                }
            }
        }
    }
}
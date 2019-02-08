package org.coner.drs.domain.entity

data class RegistrationHint(val category: String, val handicap: String, val number: String)

object RegistrationHintMapper {
    fun fromRegistration(registration: Registration) = RegistrationHint(
            category = registration.category,
            handicap = registration.handicap,
            number = registration.number
    )

    fun toRegistration(registrationHint: RegistrationHint) = Registration(
            category = registrationHint.category,
            handicap = registrationHint.handicap,
            number = registrationHint.number
    )
}
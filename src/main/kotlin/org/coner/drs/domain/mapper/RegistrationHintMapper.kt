package org.coner.drs.domain.mapper

import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RegistrationHint

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

    fun toNumbersFieldSuggestion(registrationHint: RegistrationHint): String {
        return listOf(registrationHint.number, registrationHint.category, registrationHint.handicap)
                .filter { it.isNotBlank() }
                .joinToString(" ")
    }
}
package org.coner.drs.domain.service

import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.DriverAutoCompleteOrderPreference
import org.coner.drs.domain.entity.RegistrationHint
import org.coner.drs.domain.entity.RegistrationHintMapper
import org.coner.drs.util.levenshtein
import tornadofx.*
import kotlin.streams.toList

class RegistrationService : Controller() {

    fun buildNumbersFieldHints(
            numbersField: String,
            registrationHints: Set<RegistrationHint>,
            autoCompleteOrderPreference: DriverAutoCompleteOrderPreference
    ): List<String> {
        if (numbersField.isBlank()) return emptyList()
        val registrationHints = synchronized(registrationHints) { registrationHints.toList() }
        val converter = autoCompleteOrderPreference.stringConverter
        return registrationHints.parallelStream()
                .map { converter.toString(it) }
                .filter { it.startsWith(numbersField) }
                .sorted { left, right -> levenshtein(left, right) }
                .toList()
    }

    fun buildRegistrationHints(registrations: List<Registration>): List<RegistrationHint> {
        return synchronized(registrations) { registrations.toList() }.parallelStream()
                .map { RegistrationHintMapper.fromRegistration(it) }
                .distinct()
                .toList()
    }

}
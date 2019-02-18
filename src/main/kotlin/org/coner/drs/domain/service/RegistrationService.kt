package org.coner.drs.domain.service

import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RegistrationHint
import org.coner.drs.domain.entity.RegistrationHintMapper
import tornadofx.*
import kotlin.streams.toList

class RegistrationService : Controller() {

    fun buildNumbersFieldHints(
            numbersField: String,
            registrationHints: Set<RegistrationHint>
    ): List<String> {
        if (numbersField.isBlank()) return emptyList()
        val registrationHints = synchronized(registrationHints) { registrationHints.toList() }
        return registrationHints.parallelStream()
                .map { RegistrationHintMapper.toNumbersFieldSuggestion(it) }
                .filter { it.startsWith(numbersField) }
                .sorted()
                .toList()
    }

    fun buildRegistrationHints(registrations: List<Registration>): List<RegistrationHint> {
        return synchronized(registrations) { registrations.toList() }.parallelStream()
                .map { RegistrationHintMapper.fromRegistration(it) }
                .distinct()
                .toList()
    }

}
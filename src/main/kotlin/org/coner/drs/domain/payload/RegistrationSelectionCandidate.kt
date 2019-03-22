package org.coner.drs.domain.payload

import org.coner.drs.domain.entity.Registration

data class RegistrationSelectionCandidate (
    val registration: Registration,
    val levenshteinDistanceToNumbersField: Int
)
package org.coner.drs.domain.service

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import org.apache.commons.text.similarity.LevenshteinDistance
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.payload.RegistrationSelectionCandidate
import tornadofx.*
import kotlin.streams.toList

class RegistrationService : Controller() {

    private val levenshteinDistance by lazy { LevenshteinDistance.getDefaultInstance() }

    fun findAutoSelectionCandidate(
            registrations: List<Registration>,
            numbersField: String
    ): RegistrationSelectionCandidate?  {
        if (numbersField.length <= 2) {
            return null
        }

        val candidates = registrations.map { RegistrationSelectionCandidate(
                registration = it,
                levenshteinDistanceToNumbersField = levenshteinDistance.apply(it.numbers, numbersField)
        ) }.sortedBy(RegistrationSelectionCandidate::levenshteinDistanceToNumbersField)
        return when (candidates.size) {
            1 -> candidates[0]
            in 2..Int.MAX_VALUE -> {
                val candidate0LevenshteinDistance = candidates[0].levenshteinDistanceToNumbersField
                val candidate1LevenshteinDistance = candidates[1].levenshteinDistanceToNumbersField
                if (candidate0LevenshteinDistance < candidate1LevenshteinDistance) {
                    candidates[0]
                } else { null }
            }
            else -> null
        }
    }

    fun findNumbersFieldTokens(numbersField: String?): List<String> {
        return numbersField?.split(" ") ?: emptyList()
    }

    fun findNumbersFieldContainsNumbersTokens(numbersTokens: List<String>?): Boolean {
        return when (numbersTokens?.size) {
            2, 3 -> numbersTokens[0].isInt() && numbersTokens.all { numberToken -> numberToken.isNotBlank() }
            else -> false
        }
    }

    fun findNumbersFieldArbitraryRegistration(numbersFieldTokens: List<String>?): Registration? {
        return when (numbersFieldTokens?.size) {
            2 -> Registration(
                    number = numbersFieldTokens[0],
                    category = "",
                    handicap = numbersFieldTokens[1]
            )
            3 -> Registration(
                    number = numbersFieldTokens[0],
                    category = numbersFieldTokens[1],
                    handicap = numbersFieldTokens[2]
            )
            else -> null
        }
    }

}

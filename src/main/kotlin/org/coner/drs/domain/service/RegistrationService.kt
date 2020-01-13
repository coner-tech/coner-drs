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
                val candidatesWithMinimumLevenshteinDistance = candidates.filter {
                    it.levenshteinDistanceToNumbersField == candidates[0].levenshteinDistanceToNumbersField
                }.sortedByDescending { it.registration.numbers.length }
                candidatesWithMinimumLevenshteinDistance.first()
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

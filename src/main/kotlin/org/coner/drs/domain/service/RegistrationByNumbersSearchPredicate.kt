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

import javafx.beans.property.StringProperty
import org.coner.drs.domain.entity.Registration
import tornadofx.*
import java.util.function.Predicate

class RegistrationByNumbersSearchPredicate(
        numbersProperty: StringProperty
) : Predicate<Registration> {

    var numbers by numbersProperty

    private val tokensBinding = numbersProperty.objectBinding {
        it?.split(" ") ?: emptyList()
    }
    private val tokens by tokensBinding

    private val tokenCharsBinding = tokensBinding.objectBinding {
        it?.singleOrNull()?.toCharArray()?.distinct()
    }
    private val tokenChars by tokenCharsBinding

    override fun test(registration: Registration): Boolean {
        return when (tokens?.size ?: 0) {
            0 -> true
            1 -> {
                tokenChars?.all { tokenChar -> registration.numbers.contains(tokenChar) } ?: false
            }
            2, 3 -> tokens?.all { registration.numbers.contains(it) } ?: false
            else -> false
        }
    }

}
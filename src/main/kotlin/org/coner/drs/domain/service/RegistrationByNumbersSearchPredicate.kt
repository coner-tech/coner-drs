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
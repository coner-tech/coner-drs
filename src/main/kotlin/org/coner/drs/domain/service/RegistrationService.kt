package org.coner.drs.domain.service

import javafx.collections.ObservableList
import org.coner.drs.domain.entity.Registration
import tornadofx.*

class RegistrationService : Controller() {

    fun search(registrations: ObservableList<Registration>, numbers: String? = ""): List<Registration> {
        val tokens = numbers?.split(" ") ?: listOf(numbers ?: "")
        return when (tokens.size) {
            1 -> {
                val tokenChars = tokens.single().toCharArray().distinct()
                registrations.filter { registration ->
                    tokenChars.all { tokenChar -> registration.numbers.contains(tokenChar) }
                }
            }
            2 -> registrations.filter { registration ->
                registration.numbers == "${tokens[0]} ${tokens[1]}"
            }
            3 -> registrations.filter { registration ->
                registration.numbers == "${tokens[0]} ${tokens[1]} ${tokens[2]}"
            }
            else -> emptyList()
        }
    }

}

package org.coner.drs.domain.service

import javafx.collections.ObservableList
import org.coner.drs.domain.entity.Registration
import tornadofx.*
import kotlin.streams.toList

class RegistrationService : Controller() {

    fun search(registrations: ObservableList<Registration>, numbers: String = ""): List<Registration> {
        val tokens = numbers.split(" ")
        val results = when (tokens.size) {
            0 -> registrations
            1 -> {
                val tokenChars = tokens.single().toCharArray().distinct()
                registrations.filter { registration ->
                    tokenChars.all { tokenChar -> registration.numbers.contains(tokenChar) }
                }
            }
            2, 3 -> registrations.filter { registration ->
                tokens.all { registration.numbers.contains(it) }
            }
            else -> emptyList()
        }
        return results.parallelStream()
                .sorted(compareBy { it.numbers })
                .toList()
    }

}

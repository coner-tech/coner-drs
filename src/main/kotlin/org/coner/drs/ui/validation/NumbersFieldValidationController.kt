package org.coner.drs.ui.validation

import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.ui.runevent.RunEventModel
import tornadofx.*

class NumbersFieldValidationController : Controller() {

    val registrationService: RegistrationService by inject()
    val model: RunEventModel by inject()

    val validator: ValidationContext.(String?) -> ValidationMessage? = { numbers ->
        val tokens = numbers?.split(" ") ?: emptyList()
        when (tokens.size) {
            2, 3 -> {
                if (tokens[0].isInt() && tokens.all { it.isNotBlank() })
                    null
                else
                    error("")
            }
            else -> {
                if (registrationService.search(model.registrations, numbers ?: "").size == 1)
                    null
                else
                    error("")
            }
        }
    }
}
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

package org.coner.drs.ui.alterdriversequence

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.transformation.SortedList
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
import org.coner.drs.domain.payload.RegistrationSelectionCandidate
import org.coner.drs.domain.service.RegistrationByNumbersSearchPredicate
import org.coner.drs.domain.service.RegistrationService
import tornadofx.*

class SpecifyDriverSequenceAlterationModel : ViewModel() {

    private val alterDriverSequenceModel: AlterDriverSequenceModel by inject()
    private val registrationService: RegistrationService by inject()

    val sequenceProperty = SimpleIntegerProperty(this, "sequence")
    var sequence by sequenceProperty

    val relativeProperty = SimpleObjectProperty<InsertDriverIntoSequenceRequest.Relative>(this, "relative", InsertDriverIntoSequenceRequest.Relative.BEFORE)
    var relative by relativeProperty

    val numbersFieldProperty = SimpleStringProperty(this, "numbers", "")
    var numbersField by numbersFieldProperty

    val registrationProperty = SimpleObjectProperty<Registration>(this, "registration")
    var registration by registrationProperty

    private val numbersFieldTokensBinding = numbersFieldProperty.objectBinding {
        registrationService.findNumbersFieldTokens(it)
    }
    val numbersFieldTokens by numbersFieldTokensBinding

    val numbersFieldContainsNumbersTokensBinding = numbersFieldTokensBinding.booleanBinding { tokens ->
        registrationService.findNumbersFieldContainsNumbersTokens(tokens)
    }
    val numbersFieldContainsNumbersTokens by numbersFieldContainsNumbersTokensBinding

    val numbersFieldArbitraryRegistrationBinding = numbersFieldTokensBinding.objectBinding {
        registrationService.findNumbersFieldArbitraryRegistration(it)
    }
    val numbersFieldArbitraryRegistrationProperty = SimpleObjectProperty<Registration>(this, "numbersFieldArbitraryRegistration").apply {
        bind(numbersFieldArbitraryRegistrationBinding)
    }

    val numbersFieldArbitraryRegistration by numbersFieldArbitraryRegistrationProperty
    private val registrationListPredicate = RegistrationByNumbersSearchPredicate(numbersFieldProperty)

    val registrationList = SortedFilteredList(
            items = SortedList(alterDriverSequenceModel.event.registrations, compareBy(Registration::numbers))
    ).apply {
        filterWhen(numbersFieldProperty) { query, item ->
            registrationListPredicate.test(item)
        }
    }

    val registrationListAutoSelectionBinding = objectBinding(registrationList) {
        registrationService.findAutoSelectionCandidate(this, numbersField)
    }
    val registrationListAutoSelectionCandidateProperty = SimpleObjectProperty<RegistrationSelectionCandidate>(this, "registrationListAutoSelectionCandidate").apply {
        bind(registrationListAutoSelectionBinding)
    }
    var registrationListAutoSelectionCandidate by registrationListAutoSelectionCandidateProperty

}
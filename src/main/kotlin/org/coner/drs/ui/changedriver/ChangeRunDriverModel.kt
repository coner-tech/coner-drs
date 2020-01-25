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

package org.coner.drs.ui.changedriver

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import javafx.collections.transformation.SortedList
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.payload.RegistrationSelectionCandidate
import org.coner.drs.domain.service.RegistrationByNumbersSearchPredicate
import org.coner.drs.domain.service.RegistrationService
import tornadofx.*

class ChangeRunDriverModel(
        val run: Run,
        private val registrations: ObservableList<Registration>
) : ViewModel() {

    private val registrationService: RegistrationService by inject()

    val numbersFieldProperty = SimpleStringProperty(this, "numbers", "")
    var numbersField by numbersFieldProperty

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
            items = SortedList(registrations, compareBy(Registration::numbers))
    ).apply {
        filterWhen(numbersFieldProperty) { query, item ->
            registrationListPredicate.test(item)
        }
    }
    val registrationListSelectionProperty = SimpleObjectProperty<Registration>(this, "registrationListSelection")

    var registrationListSelection by registrationListSelectionProperty
    val registrationListAutoSelectionCandidateProperty = SimpleObjectProperty<RegistrationSelectionCandidate>(this, "registrationListAutoSelectionCandidate")

    var registrationListAutoSelectionCandidate by registrationListAutoSelectionCandidateProperty

}
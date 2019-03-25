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
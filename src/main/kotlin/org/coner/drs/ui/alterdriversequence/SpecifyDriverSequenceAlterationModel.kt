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
import org.coner.drs.ui.runevent.RunEventModel
import tornadofx.*

class SpecifyDriverSequenceAlterationModel : ViewModel() {

    private val runEventModel: RunEventModel by inject()
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
            items = SortedList(runEventModel.event.registrations, compareBy(Registration::numbers))
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
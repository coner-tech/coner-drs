package org.coner.drs.ui.runevent

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ListChangeListener
import javafx.collections.transformation.SortedList
import org.apache.commons.text.similarity.LevenshteinDistance
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.service.RegistrationByNumbersSearchPredicate
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class AddNextDriverModel : ViewModel() {

    val runEventModel: RunEventModel by inject()

    val numbersFieldProperty = SimpleStringProperty(this, "numbers", "")
    var numbersField by numbersFieldProperty

    private val registrationListPredicate = RegistrationByNumbersSearchPredicate(numbersFieldProperty)
    val registrationList = SortedFilteredList(
            items = SortedList(runEventModel.event.registrations, compareBy(Registration::numbers))
    ).apply {
        filterWhen(numbersFieldProperty) { query, item ->
            registrationListPredicate.test(item)
        }
    }

    private val numbersFieldTokensBinding = numbersFieldProperty.objectBinding {
        it?.split(" ") ?: emptyList()
    }
    val numbersFieldTokens by numbersFieldTokensBinding

    val numbersFieldContainsNumbersTokensBinding = numbersFieldTokensBinding.booleanBinding { tokens ->
        when (tokens?.size) {
            2, 3 -> tokens[0].isInt() && tokens.all { token -> token.isNotBlank() }
            else -> false
        }
    }
    val numbersFieldContainsNumbersTokens by numbersFieldContainsNumbersTokensBinding

    val numbersFieldArbitraryRegistrationBinding = numbersFieldTokensBinding.objectBinding {
        when (it?.size) {
            2 -> Registration(number = it[0], category = "", handicap = it[1])
            3 -> Registration(number = it[0], category = it[1], handicap = it[2])
            else -> null
        }
    }
    val numbersFieldArbitraryRegistrationProperty = SimpleObjectProperty<Registration>(this, "numbersFieldArbitraryRegistration").apply {
        bind(numbersFieldArbitraryRegistrationBinding)
    }
    val numbersFieldArbitraryRegistration by numbersFieldArbitraryRegistrationProperty

    val registrationListSelectionProperty = SimpleObjectProperty<Registration>(this, "registrationListSelection")
    var registrationListSelection by registrationListSelectionProperty

    val registrationListAutoSelectionCandidateProperty = SimpleObjectProperty<SelectionCandiate>(this, "registrationListAutoSelectionCandidate")
    var registrationListAutoSelectionCandidate by registrationListAutoSelectionCandidateProperty

    init {
        registrationList.onChange { onRegistrationListChange(it) }
    }

    data class SelectionCandiate(
            val registration: Registration,
            val levenshteinDistanceToNumbersField: Int
    )

    private fun onRegistrationListChange(change: ListChangeListener.Change<out Registration>) {
        if (numbersField.length <= 2) {
            registrationListSelection = null
            return
        }

        val levenshteinDistance = LevenshteinDistance.getDefaultInstance()
        while (change.next()) {
            val selections = change.list
                    .map { SelectionCandiate(
                            registration = it,
                            levenshteinDistanceToNumbersField = levenshteinDistance.apply(it.numbers, numbersField)
                    ) }
                    .sortedBy(SelectionCandiate::levenshteinDistanceToNumbersField)
            registrationListAutoSelectionCandidate = when (selections.size) {
                1 -> selections[0]
                in 2..Int.MAX_VALUE -> {
                    if (selections[0].levenshteinDistanceToNumbersField < selections[1].levenshteinDistanceToNumbersField) {
                        selections[0]
                    } else { null }
                }
                else -> null
            }
        }
    }
}
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

package org.coner.drs.ui.runevent

import javafx.collections.ListChangeListener
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.domain.service.RunService
import tornadofx.*

class AddNextDriverController : Controller() {
    val model: AddNextDriverModel by inject()
    val view: AddNextDriverView by inject()
    val runService: RunService by inject()
    val runEventModel: RunEventModel by inject()
    val registrationService: RegistrationService by inject()

    init {
        model.registrationList.onChange { updateRegistrationListAutoSelection(it) }
    }

    fun addNextDriverFromRegistrationListSelection() {
        runService.addNextDriver(runEventModel.event, model.registrationListSelection).subscribe()
        reset()
    }

    fun addNextDriverForceExactNumbers() {
        if (!model.numbersFieldContainsNumbersTokens) return // TODO: guidance
        val registration = model.numbersFieldArbitraryRegistration
        runService.addNextDriver(runEventModel.event, registration).subscribe()
        reset()
    }

    fun reset() {
        model.numbersField = ""
        view.numbersField.requestFocus()
    }

    fun updateRegistrationListAutoSelection(registrationListChange: ListChangeListener.Change<out Registration>) {
        while (registrationListChange.next()) {
            model.registrationListAutoSelectionCandidate = registrationService.findAutoSelectionCandidate(
                    registrationListChange.list,
                    model.numbersField
            )
        }
    }

    val locateRunEventTable = { find<RunEventTableView>().table }

}

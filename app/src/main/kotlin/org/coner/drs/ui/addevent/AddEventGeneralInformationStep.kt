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

package org.coner.drs.ui.addevent

import org.coner.drs.domain.model.EventModel
import org.coner.drs.io.DrsIoController
import tornadofx.*

class AddEventGeneralInformationStepFragment : Fragment("General Information") {
    val event: EventModel by inject()

    override val root = form {
        fieldset(title) {
            field("Date") {
                datepicker(event.date) {
                    required()
                }
            }
            field("Name") {
                textfield(event.name) {
                    required()
                }
            }
        }
    }

    override val complete = event.valid(event.date, event.name)
}

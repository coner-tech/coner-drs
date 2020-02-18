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

package org.coner.drs.ui

import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RegistrationModel
import tornadofx.*
class RegistrationCellFragment : ListCellFragment<Registration>() {

    val registration = RegistrationModel().bindTo(this)

    override val root = vbox {
        prefWidth = 0.0
        label(registration.numbers) {
            tooltip {
                textProperty().bind(registration.numbers)
            }
            style {
                fontSize = 24.pt
            }
        }
        label(registration.name) {
            tooltip {
                textProperty().bind(registration.name)
            }
        }
        label(registration.carModel) {
            tooltip {
                textProperty().bind(registration.carModel)
            }
        }
        label(registration.carColor) {
            tooltip {
                textProperty().bind(registration.carColor)
            }
        }
    }


}
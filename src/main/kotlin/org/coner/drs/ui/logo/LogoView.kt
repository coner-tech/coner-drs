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

package org.coner.drs.ui.logo

import org.coner.drs.ui.DrsStylesheet
import tornadofx.*

class LogoView : Fragment() {
    val size: Size by param(Size.X96)

    override val root = stackpane {
        addClass(DrsStylesheet.logo)
        imageview(url = "/coner-logo/coner-logo_$size.png") {
            setId(DrsStylesheet.logoImage)
        }
    }

    enum class Size {
        X96,
        X128,
        X1024;

        override fun toString(): String {
            return name.substring(1)
        }}
}

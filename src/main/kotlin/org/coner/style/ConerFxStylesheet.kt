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

package org.coner.style

import javafx.scene.paint.Color
import javafx.scene.paint.Stop
import javafx.scene.text.FontSmoothingType
import javafx.scene.text.FontWeight
import tornadofx.*


class ConerFxStylesheet : Stylesheet() {

    companion object {
        val h1 by cssclass("h1")
        val h2 by cssclass("h2")
    }

    init {
        root {
            accentColor = ConerLogoPalette.ORANGE
            focusColor = ConerLogoPalette.ORANGE
        }
        text {
            fontSmoothingType = FontSmoothingType.GRAY
        }
        h1 {
            fontSize = 24.pt
            fontWeight = FontWeight.BOLD
            padding = box(0.px, 8.px)
        }
        h2 {
            fontSize = 18.pt
            fontWeight = FontWeight.BOLD
            padding = box(0.px, 8.px)
        }
        select(button.and(default)) {
            baseColor = ConerLogoPalette.Triad.SECONDARY_1
        }
    }
}
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

import javafx.scene.paint.Color
import org.coner.style.ConerFxStylesheet
import org.coner.style.ConerLogoPalette
import tornadofx.*

class DrsStylesheet : Stylesheet(ConerFxStylesheet::class) {

    companion object {
        val logo by cssclass("logo")
        val icon by cssclass("icon")
        val logoImage by cssid("image")
        val penalties by cssclass("penalties")
        val topBar by cssclass("top-bar")
    }

    init {
        topBar {
            label {
                textFill = Color.WHITE
            }
            padding = box(16.px)
            backgroundColor = multi(ConerLogoPalette.DARK_GRAY)
            menuBar {
                backgroundColor = multi(Color.TRANSPARENT)
            }
            menu {
                label {
                    textFill = Color.WHITE
                }
            }
            menuItem {
                label {
                    textFill = Color.BLACK
                }
                and(focused) {
                    label {
                        textFill = Color.WHITE
                    }
                }
            }
        }
    }

}
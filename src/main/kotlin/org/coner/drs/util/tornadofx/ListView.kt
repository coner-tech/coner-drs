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

package org.coner.drs.util.tornadofx

import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import tornadofx.*

/**
 * Capture up and down button presses from the passed TextField while it has
 * focus and make selection changes on this ListView.
 */
fun <T : Any> ListView<T>.takeVerticalArrowKeyPressesAsSelectionsFrom(textField: TextField) {
    textField.setOnKeyPressed {
        check(selectionModel.selectionMode == SelectionMode.SINGLE) {
            "Only supports single selection mode"
        }
        if (items.isNullOrEmpty()) {
            return@setOnKeyPressed
        }
        when (it.code) {
            KeyCode.DOWN, KeyCode.UP -> {
                if (selectionModel.selectedIndex >= 0) {
                    if (it.code == KeyCode.DOWN && selectionModel.selectedIndex < items.lastIndex) {
                        selectionModel.selectNext()
                    } else if (it.code == KeyCode.UP && selectionModel.selectedIndex > 0) {
                        selectionModel.selectPrevious()
                    }
                } else {
                    selectionModel.select(0)
                }
                it.consume()
                scrollTo(selectedItem)
            }
            else -> { /* no-op */ }
        }
    }
}
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

package org.coner.drs.test.testfx

import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.Labeled
import javafx.scene.input.KeyCode
import org.hamcrest.Matcher
import org.testfx.api.FxRobot

inline fun <reified T> FxRobot.chooseInChoiceBox(
        choiceBox: ChoiceBox<T>,
        matcher: Matcher<Labeled>,
        maxAttempts: Int? = null
) {
    var matched = false
    val max = maxAttempts ?: choiceBox.items.size + 1
    var attempts = 0
    val label = from(choiceBox).lookup(".label").query<Label>()
    while (!matched) {
        if (attempts > max) {
            throw IllegalStateException("Exceeded max attempts")
        }
        clickOn(choiceBox)
        type(KeyCode.DOWN)
        type(KeyCode.ENTER)
        matched = matcher.matches(label)
        attempts++
    }
}
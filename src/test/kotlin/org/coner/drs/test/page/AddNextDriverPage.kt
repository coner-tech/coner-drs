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

package org.coner.drs.test.page

import javafx.scene.control.ListView
import javafx.scene.control.MenuItem
import javafx.scene.control.SplitMenuButton
import javafx.scene.control.TextInputControl
import org.coner.drs.domain.entity.Registration
import tornadofx.*

interface AddNextDriverPage {

    fun root(): Form

    fun sequenceField() : TextInputControl

    fun numbersField(): TextInputControl

    fun writeInNumbersField(s: String)

    fun registrationsListView(): ListView<Registration>

    fun focusRegistrationsListView()

    fun selectRegistration(registration: Registration)

    fun focusNumbersField()

    fun addButton(): SplitMenuButton

    fun doAddSelectedRegistration()

    fun addForceExactNumbersItem(): MenuItem

    fun doAddForceExactNumbers()
}
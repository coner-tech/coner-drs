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

package org.coner.drs.test.page.real

import javafx.scene.control.SplitMenuButton
import javafx.scene.control.TextInputControl
import javafx.scene.input.KeyCode
import org.coner.drs.domain.entity.Registration
import org.coner.drs.test.page.AddNextDriverPage
import org.testfx.api.FxRobot
import tornadofx.*

open class RealAddNextDriverPage(
        protected val robot: FxRobot
) : AddNextDriverPage {

    override fun root() = robot.lookup("#add-next-driver")
            .queryAs(Form::class.java)!!

    override fun sequenceField() = robot.from(root())
            .lookup("#sequence")
            .queryTextInputControl()

    override fun numbersField() = robot.from(root())
            .lookup("#numbers")
            .queryTextInputControl()!!

    override fun focusNumbersField() {
        robot.clickOn(numbersField())
    }

    override fun writeInNumbersField(s: String) {
        focusNumbersField()
        robot.write(s)
    }

    override fun registrationsListView() = robot.from(root())
            .lookup("#registrations-list-view")
            .queryListView<Registration>()!!

    override fun focusRegistrationsListView() {
        robot.clickOn(registrationsListView())
    }

    override fun selectRegistration(registration: Registration) {
        focusNumbersField()
        while (registrationsListView().selectionModel.selectedItem != registration) {
            val lastSelectedItem = registrationsListView().selectionModel.selectedItem
            robot.type(KeyCode.DOWN)
            if (registrationsListView().selectionModel.selectedItem == lastSelectedItem)
                throw Exception("Reached end of registrations without finding a match")
        }
    }

    override fun addButton() = robot.from(root())
            .lookup("#add")
            .queryAs(SplitMenuButton::class.java)!!

    override fun doAddSelectedRegistration() {
        robot.clickOn(addButton())
    }

    override fun addForceExactNumbersItem() = addButton().items.first { it.id == "force-exact-numbers" }

    override fun doAddForceExactNumbers() {
        robot.press(KeyCode.CONTROL)
        robot.type(KeyCode.ENTER)
        robot.release(KeyCode.CONTROL)
    }
}
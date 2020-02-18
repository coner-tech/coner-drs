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

import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import org.coner.drs.domain.entity.Event
import org.coner.drs.test.page.ChooseEventPage
import org.testfx.api.FxRobot

open class RealChooseEventPage(protected val robot: FxRobot) : ChooseEventPage {

    override fun root() = robot.lookup("#choose-event")
            .query() as BorderPane

    override fun startButton() = robot.from(root())
            .lookup("#start")
            .queryButton()

    override fun eventsTable() = robot.from(root())
            .lookup("#events")
            .query() as TableView<Event>

    override fun selectEvent(matcher: (Event) -> Boolean) {
        throw UnsupportedOperationException()
    }

    override fun clickStartButton() {
        robot.clickOn(startButton())
    }
}
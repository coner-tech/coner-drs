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

package org.coner.drs.ui.runevent

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.layout.Priority
import org.coner.drs.ui.DrsStylesheet
import org.coner.drs.ui.logo.LogoView
import tornadofx.*

class RunEventTopView : View() {

    private val controller: RunEventTopController by inject()
    private val model: RunEventModel by inject()
    var fileExit: MenuItem by singleAssign()
    var reportsAuditList: MenuItem by singleAssign()
    var helpAbout: MenuItem by singleAssign()
    var eventName: Label by singleAssign()

    override val root = hbox(
            spacing = 16,
            alignment = Pos.CENTER_LEFT
    ) {
        id = "run-event-top-view"
        add<LogoView>()
        menubar {
            menu("File") {
                item("Exit") {
                    fileExit = this
                }
            }
            menu("Reports") {
                item("Audit List") {
                    reportsAuditList = this
                }
            }
            menu("Help") {
                item("About") {
                    helpAbout = this
                }
            }
        }
        pane {
            hgrow = Priority.ALWAYS
        }
        label(model.eventProperty.select { it.nameProperty }) {
            eventName = this
            id = "event-name"
        }
        addClass(DrsStylesheet.topBar)
    }

    override fun onDock() {
        super.onDock()
        controller.docked()
    }

    override fun onUndock() {
        super.onUndock()
        controller.undocked()
    }
}
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

package org.coner.drs.ui.alterdriversequence

import javafx.scene.control.ButtonBar
import org.coner.drs.ui.runevent.RunEventModel
import tornadofx.*

class AlterDriverSequenceView : View() {

    private val model: AlterDriverSequenceModel by inject()
    private val controller: AlterDriverSequenceController by inject()

    override val root = borderpane {
        id = "alter-driver-sequence-view"
        left {
            add<SpecifyDriverSequenceAlterationView>()
        }
        center {
            add<PreviewAlteredDriverSequenceView>()
        }
        bottom {
            buttonbar {
                id = "bottom-buttonbar"
                button(text = "OK", type = ButtonBar.ButtonData.OK_DONE) {
                    id = "ok"
                    isDefaultButton = true
                    action {
                        runAsyncWithProgress {
                            controller.executeAlterDriverSequence()
                        } success {
                            model.result = it
                            close()
                        }
                    }
                }
                button(text = "Cancel", type = ButtonBar.ButtonData.CANCEL_CLOSE) {
                    id = "cancel"
                    action {
                        close()
                    }
                }
            }
        }

    }

    override fun onDock() {
        super.onDock()
        title = "Insert Driver Into Sequence"
        fire(ResetEvent())
    }

}
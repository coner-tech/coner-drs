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

import javafx.scene.layout.Priority
import javafx.util.StringConverter
import org.coner.drs.ui.DrsStylesheet
import tornadofx.*

class PreviewAlteredDriverSequenceView : View() {

    private val model: PreviewAlteredDriverSequenceModel by inject()
    private val controller: PreviewAlteredDriverSequenceController = find()

    override val root = form {
        id = "preview-altered-driver-sequence-view"
        fieldset("Preview") {
            vgrow = Priority.ALWAYS
            tableview(model.previewResultRunsProperty) {
                id = "runs-table"
                isEditable = false
                setSortPolicy { false }
                vgrow = Priority.ALWAYS
                column("Sequence", PreviewAlteredDriverSequenceResult.Run::sequenceProperty)
                column("Status", PreviewAlteredDriverSequenceResult.Run::statusProperty) {
                    converter(object : StringConverter<PreviewAlteredDriverSequenceResult.Status>() {
                        override fun toString(p0: PreviewAlteredDriverSequenceResult.Status?): String {
                            return when(p0) {
                                PreviewAlteredDriverSequenceResult.Status.SAME, null -> ""
                                PreviewAlteredDriverSequenceResult.Status.INSERTED -> "Inserted"
                                PreviewAlteredDriverSequenceResult.Status.SHIFTED -> "Shifted"
                            }
                        }

                        override fun fromString(p0: String?): PreviewAlteredDriverSequenceResult.Status {
                            throw UnsupportedOperationException()
                        }
                    })
                }
                column("Numbers", PreviewAlteredDriverSequenceResult.Run::numbersProperty)
                column("Name", PreviewAlteredDriverSequenceResult.Run::nameProperty)
                column("Car Model", PreviewAlteredDriverSequenceResult.Run::carModelProperty)
                column("Car Color", PreviewAlteredDriverSequenceResult.Run::carColorProperty)
                column("Time", PreviewAlteredDriverSequenceResult.Run::rawTimeProperty)
                column("Penalties", PreviewAlteredDriverSequenceResult.Run::compositePenaltyProperty) {
                    cellFormat { penalties ->
                        graphic = flowpane {
                            addClass(DrsStylesheet.penalties)
                            text(penalties.disqualifiedProperty.stringBinding { if (it == true) "Disqualified" else null }) {
                                addClass(Stylesheet.text)
                                managedWhen { textProperty().isNotNull }
                            }
                            text(penalties.didNotFinishProperty.stringBinding { if (it == true) "Did Not Finish" else null }) {
                                addClass(Stylesheet.text)
                                managedWhen { textProperty().isNotNull }
                                strikethroughProperty().bind(
                                        penalties.disqualifiedProperty
                                )
                            }
                            text(penalties.rerunProperty.stringBinding { if (it == true) "Re-Run" else null }) {
                                addClass(Stylesheet.text)
                                managedWhen { textProperty().isNotNull }
                                strikethroughProperty().bind(
                                        penalties.disqualifiedProperty
                                                .or(penalties.didNotFinishProperty)
                                )
                            }
                            text(penalties.conesProperty.stringBinding { if (it?.toInt() ?: -1 > 0) "+$it" else null }) {
                                addClass(Stylesheet.text)
                                managedWhen { textProperty().isNotNull }
                                strikethroughProperty().bind(
                                        penalties.disqualifiedProperty
                                                .or(penalties.didNotFinishProperty)
                                                .or(penalties.rerunProperty)
                                )
                            }
                        }
                    }
                }
                smartResize()
                model.previewResultProperty.onChange { selectionModel.select(it?.inserted) }
            }
        }
    }
}

package org.coner.drs.ui.alterdriversequence

import javafx.scene.layout.Priority
import javafx.util.StringConverter
import org.coner.drs.DrsStylesheet
import tornadofx.*

class PreviewAlteredDriverSequenceView : View() {

    private val model: PreviewAlteredDriverSequenceModel by inject()
    private val controller: PreviewAlteredDriverSequenceController = find()

    override val root = form {
        id = "preview-altered-driver-sequence-view"
        fieldset("Preview") {
            vgrow = Priority.ALWAYS
            tableview(model.previewResultProperty.select { it.runsProperty }) {
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

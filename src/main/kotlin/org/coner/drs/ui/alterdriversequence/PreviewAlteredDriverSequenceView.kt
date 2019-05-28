package org.coner.drs.ui.alterdriversequence

import javafx.scene.layout.Priority
import javafx.util.StringConverter
import org.coner.drs.DrsStylesheet
import tornadofx.*

class PreviewAlteredDriverSequenceView : View() {

    private val model: PreviewAlteredDriverSequenceModel by inject()

    init {
        find<PreviewAlteredDriverSequenceController>()
    }

    override val root = form {
        fieldset("Preview") {
            vgrow = Priority.ALWAYS
            tableview(model.previewResultProperty.select { it.runsProperty }) {
                id = "runs-table"
                isEditable = false
                setSortPolicy { false }
                vgrow = Priority.ALWAYS
                column("Sequence", PreviewAlteredDriverSequenceResult.Run::sequence)
                column("Status", PreviewAlteredDriverSequenceResult.Run::status) {
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
                column("Numbers", PreviewAlteredDriverSequenceResult.Run::numbers)
                column("Name", PreviewAlteredDriverSequenceResult.Run::name)
                column("Car Model", PreviewAlteredDriverSequenceResult.Run::carModel)
                column("Car Color", PreviewAlteredDriverSequenceResult.Run::carColor)
                column("Time", PreviewAlteredDriverSequenceResult.Run::rawTime)
                column("Penalties", PreviewAlteredDriverSequenceResult.Run::compositePenalty) {
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
            }
        }
    }
}

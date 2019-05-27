package org.coner.drs.ui.alterdriversequence

import javafx.scene.layout.Priority
import org.coner.drs.DrsStylesheet
import org.coner.drs.domain.entity.Run
import tornadofx.*

class PreviewAlteredDriverSequenceView : View() {

    private val alterDriverSequenceModel: AlterDriverSequenceModel by inject()
    private val model: PreviewAlteredDriverSequenceModel by inject()

    override val root = form {
        fieldset("Preview") {
            vgrow = Priority.ALWAYS
            tableview(model.runsProperty) {
                id = "runs-table"
                isEditable = false
                setSortPolicy { false }
                vgrow = Priority.ALWAYS
                column("Sequence", Run::sequenceProperty)
                column("Numbers", Run::registrationNumbersProperty)
                column("Name", Run::registrationDriverNameProperty)
                column("Car Model", Run::registrationCarModelProperty)
                column("Car Color", Run::registrationCarColorProperty)
                column("Time", Run::rawTimeProperty) {

                }
                column("Penalties", Run::compositePenaltyProperty) {
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

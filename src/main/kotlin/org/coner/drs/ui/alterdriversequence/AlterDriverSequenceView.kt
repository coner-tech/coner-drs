package org.coner.drs.ui.alterdriversequence

import javafx.scene.control.ButtonBar
import org.coner.drs.ui.runevent.RunEventModel
import tornadofx.*

class AlterDriverSequenceView : View() {

    private val model: AlterDriverSequenceModel by inject()
    private val controller: AlterDriverSequenceController by inject()

    override val root = borderpane {
        left {
            add<SpecifyDriverSequenceAlterationView>()
        }
        center {
            add<PreviewAlteredDriverSequenceView>()
        }
        bottom {
            buttonbar {
                button(text = "OK", type = ButtonBar.ButtonData.OK_DONE) {
                    isDefaultButton = true
                    action {
                        runAsyncWithProgress {
                            controller.executeAlterDriverSequence()
                        } success {
                            close()
                        }
                    }
                }
                button(text = "Cancel", type = ButtonBar.ButtonData.CANCEL_CLOSE)
            }
        }

    }

    override fun onDock() {
        super.onDock()
        title = "Insert Driver Into Sequence"
    }

}
package org.coner.drs.ui.start

import javafx.scene.control.Button
import tornadofx.*

class StartCenterView : View() {

    private val model: StartModel by inject()

    var rawSheetDatabaseChooseButton: Button by singleAssign()
    var crispyFishDatabaseChooseButton: Button by singleAssign()
    var startButton: Button by singleAssign()

    override val root = stackpane {
        id = "start"
        form {
            fieldset("Databases") {
                field("Coner Digital Raw Sheet") {
                    textfield(model.rawSheetDatabaseProperty.stringBinding { it?.toString() ?: "" }) {
                        id = "raw-sheet-database-field"
                        isEditable = false
                    }
                    button("Choose") {
                        rawSheetDatabaseChooseButton = this
                    }
                }
                field("Crispy Fish") {
                    textfield(model.crispyFishDatabaseProperty.stringBinding { it?.absolutePath ?: ""}) {
                        id = "crispy-fish-database-field"
                        isEditable = false
                    }
                    button("Choose") {
                        crispyFishDatabaseChooseButton = this
                    }
                }
            }
            fieldset("Publish/Subscribe") {
                field("Subscriber") {
                    checkbox(property = model.subscriberProperty)
                }
            }
            buttonbar {
                button("Start") {
                    startButton = this
                    id = "start-button"
                    enableWhen { model.valid }
                    isDefaultButton = true
                }
            }
        }
    }
}
package org.coner.drs.ui.start

import tornadofx.*

class StartView : View("Start") {
    val controller: StartController by inject()
    val model: StartModel by inject()

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
                        action {
                            controller.onClickChooseRawSheetDatabase()
                        }
                    }
                }
                field("Crispy Fish") {
                    textfield(model.crispyFishDatabaseProperty.stringBinding { it?.absolutePath ?: ""}) {
                        id = "crispy-fish-database-field"
                        isEditable = false
                    }
                    button("Choose") {
                        action {
                            controller.onClickChooseCrispyFishDatabase()
                        }
                    }
                }
            }
            buttonbar {
                button("Start") {
                    id = "start-button"
                    enableWhen { model.valid }
                    action {
                        println("start-button clicked")
                        controller.onClickStart()
                    }
                    isDefaultButton = true
                }
            }
        }
    }
}
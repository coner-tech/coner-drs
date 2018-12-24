package org.coner.drs.ui.start

import tornadofx.*

class StartView : View("Start") {
    val controller: StartController by inject()
    val model: StartModel by inject()

    override val root = stackpane {
        form {
            fieldset("Databases") {
                field("Coner Digital Raw Sheet") {
                    textfield(model.rawSheetDatabaseProperty.stringBinding { it?.absolutePath ?: "" }) {
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
                    enableWhen { model.valid }
                    action {
                        controller.onClickStart()
                    }
                    isDefaultButton = true
                }
            }
        }
    }
}
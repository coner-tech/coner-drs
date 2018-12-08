package org.coner.drs.ui.start

import tornadofx.*

class StartView : View("Start") {
    val controller: StartController by inject()
    val model: StartModel by inject()

    override val root = stackpane {
        form {
            fieldset("Paths") {
                field("Raw Sheet Database") {
                    text(model.rawSheetDatabaseProperty.stringBinding { it?.absolutePath ?: "" })
                    button("Choose") {
                        action {
                            controller.onClickChooseRawSheetDatabase()
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
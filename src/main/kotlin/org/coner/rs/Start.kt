package org.coner.rs

import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

class StartView : View() {
    val controller: StartController by inject()
    val model: StartModel by inject()

    override val root = stackpane {
        form {
            fieldset("Paths") {
                field("Raw Sheet Database") {
                    textfield(model.rawSheetDatabaseProperty) {
                        required()
                    }
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
                }
            }
        }
    }
}

class StartController : Controller() {
    val model: StartModel by inject()

    fun onClickChooseRawSheetDatabase() {
        val dir = chooseDirectory("") ?: return
        model.rawSheetDatabase = dir.absolutePath
    }

    fun onClickStart() {
        fire(ChangeToScreenEvent(Screen.ChooseEvent(File(model.rawSheetDatabase))))
    }
}

class StartModel : ViewModel() {
    val rawSheetDatabaseProperty = SimpleStringProperty(this, "rawSheetDatabase")
    var rawSheetDatabase by rawSheetDatabaseProperty
}

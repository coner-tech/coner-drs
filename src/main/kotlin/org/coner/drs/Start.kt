package org.coner.drs

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue
import java.io.File

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

class StartController : Controller() {
    val model: StartModel by inject()

    fun onClickChooseRawSheetDatabase() {
        val dir = chooseDirectory("Raw Sheet Database") ?: return
        model.rawSheetDatabase = dir
    }

    fun onClickStart() {
        fire(ChangeToScreenEvent(Screen.ChooseEvent(model.rawSheetDatabase)))
    }
}

class StartModel : ViewModel() {
    val rawSheetDatabaseProperty = SimpleObjectProperty<File>(this, "rawSheetDatabase")
    var rawSheetDatabase by rawSheetDatabaseProperty
}

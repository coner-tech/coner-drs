package org.coner.drs.ui.choose_event

import javafx.scene.layout.Priority
import tornadofx.*

class ChooseEventBottomView : View() {
    val model: ChooseEventModel by inject()
    val controller: ChooseEventController by inject()
    override val root = hbox {
        button("New") {
            action { controller.addEvent() }
        }
        pane {
            hgrow = Priority.ALWAYS
        }
        button("Start") {
            enableWhen(model.choiceProperty.isNotNull)
            action {
                controller.onClickStart()
            }
            isDefaultButton = true
        }
    }
}
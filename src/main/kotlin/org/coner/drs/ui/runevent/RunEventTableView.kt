package org.coner.drs.ui.runevent

import javafx.scene.input.KeyCombination
import javafx.scene.layout.Priority
import org.coner.drs.domain.entity.Run
import tornadofx.*

class RunEventTableView : View() {
    val model: RunEventTableModel by inject()
    val controller: RunEventTableController by inject()

    init {
        controller.init()
    }

    override val root = tableview(model.runsSortedBySequence) {
        isEditable = true
        setSortPolicy { false }
        vgrow = Priority.ALWAYS
        readonlyColumn("Sequence", Run::sequence)
        column("Numbers", Run::registrationNumbersProperty)
        column("Name", Run::registrationDriverNameProperty)
        column("Car Model", Run::registrationCarModelProperty)
        column("Car Color", Run::registrationCarColorProperty)
        column("Time", Run::rawTime) {
        }
        column("Cones", Run::conesProperty) {
        }
        column("+/-", Run::conesProperty).cellFormat {
            graphic = hbox {
                button("+") {
                    action { controller.incrementCones(rowItem) }
                }
                button("-") {
                    action { controller.decrementCones(rowItem) }
                    enableWhen { rowItem.conesProperty.greaterThan(0) }
                }
            }
        }
        column("Did Not Finish", Run::didNotFinish) {
            makeEditable()
            setOnEditCommit { controller.changeDidNotFinish(it.rowValue, it.newValue) }
        }
        column("Re-Run", Run::rerun) {
            makeEditable()
            setOnEditCommit { controller.changeRerun(it.rowValue, it.newValue) }
        }
        column("Disqualified", Run::disqualified) {
            makeEditable()
            setOnEditCommit { controller.changeDisqualified(it.rowValue, it.newValue) }
        }
        smartResize()
        contextmenu {
            item(
                    name = "Change Driver",
                    keyCombination = KeyCombination.keyCombination("Ctrl+D")
            ) {
                enableWhen(selectionModel.selectedItemProperty().isNotNull)
                action {
                    val run = selectedItem ?: return@action
                    controller.showChangeDriver(run)
                }
            }
        }
    }

}
package org.coner.drs.ui.runevent

import javafx.scene.input.KeyCombination
import javafx.scene.layout.Priority
import org.coner.drs.domain.entity.Run
import tornadofx.*

class RunEventTableView : View() {
    val model: RunEventModel by inject()
    val controller: RunEventTableController by inject()
    val runEventController: RunEventController by inject()

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
            setOnEditCommit { controller.onEditCommitBooleanPenalty(it) }
        }
        column("Re-Run", Run::rerun) {
            makeEditable()
            setOnEditCommit { controller.onEditCommitBooleanPenalty(it) }
        }
        column("Disqualified", Run::disqualified) {
            makeEditable()
            setOnEditCommit { controller.onEditCommitBooleanPenalty(it) }
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
package org.coner.drs.ui.runevent

import javafx.scene.layout.Priority
import org.coner.drs.Run
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
        column("Category", Run::registrationCategoryProperty) {
            makeEditable()
            setOnEditCommit { controller.onEditCommitRegistrationCategory(it) }
        }
        column("Handicap", Run::registrationHandicapProperty) {
            makeEditable()
            setOnEditCommit { controller.onEditCommitRegistrationHandicap(it) }
        }
        column("Number", Run::registrationNumberProperty) {
            makeEditable()
            setOnEditCommit { controller.onEditCommitRegistrationNumber(it) }
        }
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
                    action { runEventController.incrementCones(this@cellFormat.rowItem) }
                }
                button("-") {
                    action { runEventController.decrementCones(this@cellFormat.rowItem) }
                    enableWhen { this@cellFormat.rowItem.conesProperty.greaterThan(0) }
                }
            }
        }
        column("Did Not Finish", Run::didNotFinish) {
            makeEditable()
            setOnEditCommit { controller.onEditCommitBooleanPenalty(it) }
        }
        column("Disqualified", Run::disqualified) {
            makeEditable()
            setOnEditCommit { controller.onEditCommitBooleanPenalty(it) }
        }
        column("Re-Run", Run::rerun) {
            makeEditable()
            setOnEditCommit { controller.onEditCommitBooleanPenalty(it) }
        }
        smartResize()
    }

}
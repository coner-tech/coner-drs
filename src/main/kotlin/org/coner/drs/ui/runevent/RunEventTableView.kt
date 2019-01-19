package org.coner.drs.ui.runevent

import javafx.collections.transformation.SortedList
import javafx.scene.layout.Priority
import org.coner.drs.Run
import tornadofx.*

class RunEventTableView : View() {
    val model: RunEventModel by inject()
    val controller: RunEventController by inject()

    override val root = tableview(SortedList(model.runs, compareBy(Run::sequence))) {
        isEditable = true
        setSortPolicy { false }
        vgrow = Priority.ALWAYS
        readonlyColumn("Sequence", Run::sequence)
        column("Category", Run::registrationCategoryProperty) {
        }
        column("Handicap", Run::registrationHandicapProperty) {
        }
        column("Number", Run::registrationNumberProperty) {
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
                    action { controller.incrementCones(this@cellFormat.rowItem) }
                }
                button("-") {
                    action { controller.decrementCones(this@cellFormat.rowItem) }
                    enableWhen { this@cellFormat.rowItem.conesProperty.greaterThan(0) }
                }
            }
        }
        column("Did Not Finish", Run::didNotFinish) {
            makeEditable()
        }
        column("Disqualified", Run::disqualified) {
            makeEditable()
        }
        column("Re-Run", Run::rerun) {
            makeEditable()
        }
        smartResize()
        onEditCommit {
            controller.save(it)
        }
    }

}
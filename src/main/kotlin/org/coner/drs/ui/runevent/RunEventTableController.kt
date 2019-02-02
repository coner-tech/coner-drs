package org.coner.drs.ui.runevent

import javafx.event.EventHandler
import javafx.scene.control.TableColumn
import org.coner.drs.Registration
import org.coner.drs.Run
import tornadofx.*

class RunEventTableController : Controller() {
    val controller: RunEventController by inject()

    fun onEditCommitRegistrationCategory(event: TableColumn.CellEditEvent<Run, String>) {
        val run = event.rowValue
        run.registration = run.registration.clone(category = event.newValue)
        controller.save(run)
    }

    fun onEditCommitRegistrationHandicap(event: TableColumn.CellEditEvent<Run, String>) {
        val run = event.rowValue
        run.registration = run.registration.clone(handicap = event.newValue)
        controller.save(run)
    }

    fun onEditCommitRegistrationNumber(event: TableColumn.CellEditEvent<Run, String>) {
        val run = event.rowValue
        run.registration = run.registration.clone(number = event.newValue)
        controller.save(run)
    }

    fun onEditCommitBooleanPenalty(event: TableColumn.CellEditEvent<Run, Boolean>) {
        val run = event.rowValue
        event.tableColumn.setValue(run, event.newValue)
        controller.save(run)
    }

}
package org.coner.drs.ui.runevent

import javafx.scene.control.TableColumn
import org.coner.drs.domain.entity.Run
import org.coner.drs.ui.changedriver.ChangeRunDriverFragment
import org.coner.drs.ui.changedriver.ChangeRunDriverScope
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

    fun incrementCones(run: Run) {
        run.cones++
        controller.save(run)
    }

    fun decrementCones(run: Run) {
        run.cones--
        controller.save(run)
    }

    fun showChangeDriver(run: Run) {
        val runEventModel: RunEventModel = find()
        val addNextDriverModel: AddNextDriverModel = find()
        val scope = ChangeRunDriverScope(
                runEventScope = scope,
                run = run,
                registrations = runEventModel.registrations,
                registrationHints = addNextDriverModel.registrationHints
        )
        find<ChangeRunDriverFragment>(scope).openModal()
    }

}
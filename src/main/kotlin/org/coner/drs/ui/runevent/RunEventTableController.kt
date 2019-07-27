package org.coner.drs.ui.runevent

import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.schedulers.Schedulers
import javafx.collections.ListChangeListener
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.service.RunService
import org.coner.drs.ui.alterdriversequence.AlterDriverSequenceController
import org.coner.drs.ui.changedriver.ChangeRunDriverFragment
import org.coner.drs.ui.changedriver.ChangeRunDriverScope
import tornadofx.*

class RunEventTableController : Controller() {
    val model: RunEventTableModel by inject()
    val view: RunEventTableView by inject()
    val controller: RunEventController by inject()
    val runService: RunService by inject()

    init {
        model.runsSortedBySequence = controller.model.event.runs.sorted(compareByDescending(Run::sequence))
        model.runsSortedBySequence.onChange { onRunsSortedBySequenceChanged(it) }
    }

    fun onRunsSortedBySequenceChanged(change: ListChangeListener.Change<out Run>) {
        while (change.next()) {
            if (change.wasAdded() && change.addedSize == 1) {
                runLater { view.table.scrollTo(0) }
            }
        }
    }

    fun onTableFocused(focused: Boolean) = runLater {
        val table = view.table
        if (focused) {
            if (table.selectedItem == null) {
                var selectIndex = table.items?.indexOfFirst { it.rawTime != null } ?: 0
                if (selectIndex > 0 && selectIndex < table.items.lastIndex) {
                    selectIndex--
                }
                table.selectionModel.select(selectIndex)
                table.scrollTo(0)
            }
        } else {
            table.selectionModel.clearSelection()
        }
    }

    fun incrementCones(run: Run) {
        runService.incrementCones(run)
                .subscribeOn(Schedulers.computation())
                .observeOnFx()
                .subscribe()
    }

    fun decrementCones(run: Run) {
        runService.decrementCones(run)
                .subscribeOn(Schedulers.computation())
                .observeOnFx()
                .subscribe()
    }

    fun changeDidNotFinish(run: Run, newValue: Boolean) {
        runService.changeDidNotFinish(run, newValue)
    }

    fun changeRerun(run: Run, newValue: Boolean) {
        runService.changeRerun(run, newValue)
                .subscribeOn(Schedulers.computation())
                .observeOnFx()
                .subscribe()
    }

    fun changeDisqualified(run: Run, newValue: Boolean) {
        runService.changeDisqualified(run, newValue)
                .subscribeOn(Schedulers.computation())
                .observeOnFx()
                .subscribe()
    }

    fun showChangeDriver(run: Run) {
        val runEventModel: RunEventModel = find()
        val scope = ChangeRunDriverScope(
                runEventScope = scope,
                run = run,
                registrations = runEventModel.event.registrations
        )
        find<ChangeRunDriverFragment>(scope).openModal()
    }

    fun showInsertDriver(run: Run) {
        val runEventModel: RunEventModel = find()
        val result = find<AlterDriverSequenceController>()
                .showAlterDriverSequenceAndWait(run.sequence, runEventModel.event)
        result?.run {
            view.selectRunById(this.insertRunId)
        }
    }

    val locateAddNextDriverNumbers = { find<AddNextDriverView>().numbersField }

}
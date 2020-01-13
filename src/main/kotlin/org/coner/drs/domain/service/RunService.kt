/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.coner.drs.domain.service

import io.reactivex.Completable
import io.reactivex.Single
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.mapper.RunMapper
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
import org.coner.drs.domain.payload.InsertDriverIntoSequenceResult
import org.coner.drs.io.gateway.RunGateway
import tornadofx.*
import java.math.BigDecimal

class RunService : Controller() {

    private val gateway: RunGateway by inject()
    private val controller: DomainServiceController by inject()

    fun findRunForNextDriver(runEvent: RunEvent): Run {
        val indexOfLastRunWithDriver = runEvent.runsBySequence.indexOfLast { it.registration != null }
        if (indexOfLastRunWithDriver >= 0) {
            return if (indexOfLastRunWithDriver in 0 until runEvent.runsBySequence.lastIndex) {
                runEvent.runsBySequence[indexOfLastRunWithDriver + 1]
            } else {
                Run(
                        event = runEvent,
                        sequence = runEvent.runsBySequence[indexOfLastRunWithDriver].sequence + 1
                )
            }
        }
        return runEvent.runsBySequence.firstOrNull { it.registration == null }
                ?: Run(
                        event = runEvent,
                        sequence = 1
                )
    }

    fun findRunForNextTime(runEvent: RunEvent): Single<Run> {
        return controller.scheduleSingle {
            findRunForNextTimeSync(runEvent)
        }
    }

    private fun findRunForNextTimeSync(runEvent: RunEvent): Run {
        val indexOfLastRunWithTime = runEvent.runsBySequence.indexOfLast { it.rawTime != null }
        return if (indexOfLastRunWithTime >= 0) {
            if (indexOfLastRunWithTime in 0 until runEvent.runsBySequence.lastIndex) {
                runEvent.runsBySequence[indexOfLastRunWithTime + 1]
            } else {
                // Consider error reporting for this scenario.
                // There has possibly been a finish trip without a run ready with a driver awaiting a time.
                // Perhaps a car managed to stage and launch without being noticed by Timing workers.
                // Recommend to hold start while resolving situation.
                Run(
                        event = runEvent,
                        sequence = runEvent.runsBySequence[indexOfLastRunWithTime].sequence + 1
                )
            }
        } else if (runEvent.runsBySequence.isNotEmpty()) {
            runEvent.runsBySequence[0]
        } else {
            Run(
                    event = runEvent,
                    sequence = 1
            )
        }
    }

    fun addNextDriver(event: RunEvent, registration: Registration): Completable {
        return controller.scheduleCompletable {
            addNextDriverSync(event, registration)
        }
    }

    private fun addNextDriverSync(event: RunEvent, registration: Registration) {
        val run = event.runForNextDriver
        run.registration = registration
        if (!event.runs.contains(run)) {
            event.runs.add(run)
        }
        event.runForNextDriverBinding.invalidate()
        saveSync(run)
    }

    fun addNextTime(event: RunEvent, time: BigDecimal): Completable {
        return controller.scheduleCompletable {
            addNextTimeSync(event, time)
        }
    }

    private fun addNextTimeSync(event: RunEvent, time: BigDecimal) {
        val run = event.runForNextTime
        run.rawTime = time
        if (!event.runs.contains(run)) {
            event.runs.add(run)
        }
        event.runForNextTimeBinding.invalidate()
        saveSync(run)
    }

    fun changeTime(run: Run, time: BigDecimal?): Completable {
        return controller.scheduleCompletable {
            changeTimeSync(run, time)
        }
    }

    fun changeTimeSync(run: Run, time: BigDecimal?) {
        run.rawTime = time
        saveSync(run)
    }

    fun incrementCones(run: Run): Completable {
        return controller.scheduleCompletable {
            incrementConesSync(run)
        }
    }

    private fun incrementConesSync(run: Run) {
        run.cones++
        saveSync(run)
    }

    fun decrementCones(run: Run): Completable {
        return controller.scheduleCompletable {
            decrementConesSync(run)
        }
    }

    private fun decrementConesSync(run: Run) {
        run.cones--
        saveSync(run)
    }

    fun changeDidNotFinish(run: Run, newValue: Boolean): Completable {
        return controller.scheduleCompletable {
            changeDidNotFinishSync(run, newValue)
        }
    }

    private fun changeDidNotFinishSync(run: Run, newValue: Boolean) {
        run.didNotFinish = newValue
        saveSync(run)
    }

    fun changeRerun(run: Run, newValue: Boolean): Completable {
        return controller.scheduleCompletable {
            changeRerunSync(run, newValue)
        }
    }

    private fun changeRerunSync(run: Run, newValue: Boolean) {
        run.rerun = newValue
        saveSync(run)
    }

    fun changeDisqualified(run: Run, newValue: Boolean): Completable {
        return controller.scheduleCompletable {
            changeDisqualifiedSync(run, newValue)
        }
    }

    private fun changeDisqualifiedSync(run: Run, newValue: Boolean) {
        run.disqualified = newValue
        saveSync(run)
    }

    fun changeDriver(run: Run, registration: Registration?): Completable {
        return controller.scheduleCompletable {
            changeDriverSync(run, registration)
        }
    }

    private fun changeDriverSync(run: Run, registration: Registration?) {
        run.registration = registration
        saveSync(run)
    }

    fun insertDriverIntoSequence(request: InsertDriverIntoSequenceRequest): Single<InsertDriverIntoSequenceResult> {
        return controller.scheduleSingle {
            insertDriverIntoSequenceSync(request)
        }
    }

    private fun insertDriverIntoSequenceSync(request: InsertDriverIntoSequenceRequest): InsertDriverIntoSequenceResult {
        val runs = request.runs
                .map { RunMapper.copy(it) }
                .toMutableList()
                .apply { sortWith(compareBy(Run::sequence)) }
        val insertSequence = when(request.relative) {
            InsertDriverIntoSequenceRequest.Relative.BEFORE -> maxOf(request.sequence, 1)
            InsertDriverIntoSequenceRequest.Relative.AFTER -> request.sequence + 1
        }
        val insertIndex = insertSequence - 1 // sequence is 1-indexed, lists are 0-indexed
        val insertRun = Run(
            event = request.event,
            registration = request.registration,
            sequence = insertSequence,
            rawTime = runs.getOrNull(insertIndex)?.rawTime
        )
        runs.add(insertIndex, insertRun)
        val shiftRuns = runs.filterIndexed { index, run ->
            index >= insertIndex && run.id != insertRun.id
        }
        shiftRuns.forEachIndexed { index, shiftRun ->
            shiftRun.sequence++
            val next = index + 1
            shiftRun.rawTime = shiftRuns.getOrNull(next)?.rawTime
        }
        val result = InsertDriverIntoSequenceResult(
                runs = runs,
                insertRunId = insertRun.id,
                shiftRunIds = shiftRuns.map { it.id }.toHashSet()
        )
        if (!request.dryRun) {
            saveSync(insertRun, *shiftRuns.toTypedArray())
        }
        return result
    }

    fun deleteRun(event: RunEvent, run: Run) {
        val runs = event.runs
                .map { RunMapper.copy(it) }
                .sortedBy(Run::sequence)
                .toMutableList()
        when (runs.removeIf { it.id == run.id }) {
            false -> return // TODO: error handling
        }
        val decrementSequenceRuns = runs.filter { it.sequence > run.sequence }
        decrementSequenceRuns.forEach {
            it.sequence = it.sequence - 1
        }
        gateway.delete(run)
        saveSync(*decrementSequenceRuns.toTypedArray())
    }

    private fun saveSync(vararg runs: Run) {
        runs.forEach { gateway.save(it) }
    }

}
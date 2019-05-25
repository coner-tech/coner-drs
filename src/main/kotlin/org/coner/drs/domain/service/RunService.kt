package org.coner.drs.domain.service

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
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

    val gateway: RunGateway by inject()

    fun findRunForNextTime(runEvent: RunEvent): Run {
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

    fun addNextDriver(event: RunEvent, registration: Registration) {
        val run = event.runForNextDriver
        run.registration = registration
        if (!event.runs.contains(run)) {
            event.runs.add(run)
        }
        event.runForNextDriverBinding.invalidate()
        syncSave(run)
    }

    fun addNextTime(event: RunEvent, time: BigDecimal) {
        val run = event.runForNextTime
        run.rawTime = time
        if (!event.runs.contains(run)) {
            event.runs.add(run)
        }
        event.runForNextTimeBinding.invalidate()
        syncSave(run)
    }

    fun incrementCones(run: Run) {
        run.cones++
        syncSave(run)
    }

    fun decrementCones(run: Run) {
        run.cones--
        syncSave(run)
    }

    fun changeDidNotFinish(run: Run, newValue: Boolean) {
        run.didNotFinish = newValue
        syncSave(run)
    }

    fun changeRerun(run: Run, newValue: Boolean) {
        run.rerun = newValue
        syncSave(run)
    }

    fun changeDisqualified(run: Run, newValue: Boolean) {
        run.disqualified = newValue
        syncSave(run)
    }

    fun changeDriver(run: Run, registration: Registration?) {
        run.registration = registration
        syncSave(run)
    }

    fun insertDriverIntoSequence(request: InsertDriverIntoSequenceRequest): Single<InsertDriverIntoSequenceResult> {
        return Single.just(request)
                .flatMap {
                    Single.just(request)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(Schedulers.computation())
                            .map { performInsertDriverIntoSequence(request) }
                }
                .flatMap { result ->
                    Single.just(result)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(Schedulers.io())
                            .map { writeInsertDriverIntoSequenceResult(request, result) }
                }
    }

    private fun performInsertDriverIntoSequence(request: InsertDriverIntoSequenceRequest): InsertDriverIntoSequenceResult {
        val runs = request.runs
                .map { RunMapper.copy(it) }
                .toMutableList()
                .apply { sortWith(compareBy(Run::sequence)) }
        val insertSequence = when(request.relative) {
            InsertDriverIntoSequenceRequest.Relative.BEFORE -> maxOf(request.sequence - 1, 1)
            InsertDriverIntoSequenceRequest.Relative.AFTER -> request.sequence + 1
        }
        val insertIndex = insertSequence - 1 // sequence is 1-indexed, lists are 0-indexed
        val insertRun = Run(
                event = request.event,
                registration = request.registration,
                sequence = insertSequence
        )
        runs.add(insertIndex, insertRun)
        val shiftRuns = runs.filterIndexed { index, run ->
            index >= insertIndex && run.id != insertRun.id
        }
        shiftRuns.forEach { run -> run.sequence++ }
        return InsertDriverIntoSequenceResult(
                runs = runs,
                insertRun = insertRun,
                shiftRuns = shiftRuns.toTypedArray()
        )
    }

    private fun writeInsertDriverIntoSequenceResult(
            request: InsertDriverIntoSequenceRequest,
            result: InsertDriverIntoSequenceResult
    ): InsertDriverIntoSequenceResult {
        if (!request.dryRun) {
            syncSave(result.insertRun, *result.shiftRuns)
        }
        return result
    }

    private fun syncSave(vararg runs: Run) {
        runs.forEach { gateway.save(it) }
    }
}
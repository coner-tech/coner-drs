package org.coner.drs.node.service

import org.coner.drs.node.db.DigitalRawSheetDatabase
import org.coner.drs.node.db.entity.RunDbEntity
import org.coner.drs.node.db.getRun
import org.coner.drs.node.payload.AlterDriverSequenceRequest
import org.coner.drs.node.payload.AlterDriverSequenceResult
import java.math.BigDecimal
import java.util.*

class RunService(
        private val database: DigitalRawSheetDatabase
) : EntityService<RunDbEntity> {

    override val resource = database.entity<RunDbEntity>()

    fun listRuns(eventId: UUID) = resource.list(eventId)

    fun findRunForNextDriver(eventId: UUID): RunDbEntity {
        val runsBySequence = listRuns(eventId).sortedBy { it.sequence }
        val indexOfLastRunWithDriver = runsBySequence.indexOfLast { it.hasDriver() }
        if (indexOfLastRunWithDriver >= 0) {
            return if (indexOfLastRunWithDriver in 0 until runsBySequence.lastIndex) {
                runsBySequence[indexOfLastRunWithDriver + 1]
            } else {
                RunDbEntity(
                        eventId = eventId,
                        sequence = runsBySequence[indexOfLastRunWithDriver].sequence + 1
                )
            }
        }
        return runsBySequence.firstOrNull { !it.hasDriver() }
                ?: RunDbEntity(
                        eventId = eventId,
                        sequence = 1
                )
    }

    fun findRunForNextTime(eventId: UUID): RunDbEntity {
        val runsBySequence = listRuns(eventId).sortedBy { it.sequence }
        val indexOfLastRunWithTime = runsBySequence.indexOfLast { it.rawTime != null }
        return if (indexOfLastRunWithTime >= 0) {
            if (indexOfLastRunWithTime in 0 until runsBySequence.lastIndex) {
                runsBySequence[indexOfLastRunWithTime + 1]
            } else {
                // Consider error reporting for this scenario.
                // There has possibly been a finish trip without a run ready with a driver awaiting a time.
                // Perhaps a car managed to stage and launch without being noticed by Timing workers.
                // Recommend to hold start while resolving situation.
                RunDbEntity(
                        eventId = eventId,
                        sequence = runsBySequence[indexOfLastRunWithTime].sequence + 1
                )
            }
        } else if (runsBySequence.isNotEmpty()) {
            runsBySequence[0]
        } else {
            RunDbEntity(
                    eventId = eventId,
                    sequence = 1
            )
        }
    }

    fun addNextDriver(
            eventId: UUID,
            category: String,
            handicap: String,
            number: String
    ): RunDbEntity {
        val runForNextDriver = findRunForNextDriver(eventId).copy(
                category = category,
                handicap = handicap,
                number = number
        )
        resource.put(runForNextDriver)
        return runForNextDriver
    }

    fun addNextTime(eventId: UUID, rawTime: BigDecimal): RunDbEntity {
        val runForNextTime = findRunForNextTime(eventId)
                .copy(rawTime = rawTime)
        resource.put(runForNextTime)
        return runForNextTime
    }

    fun changeTime(
            eventId: UUID,
            id: UUID,
            rawTime: BigDecimal?
    ): RunDbEntity {
        val run = resource.getRun(eventId = eventId, id = id)
                .copy(rawTime = rawTime)
        resource.put(run)
        return run
    }

    fun incrementCones(eventId: UUID, id: UUID): RunDbEntity {
        val incrementedRun = resource.getRun(eventId = eventId, id = id).let { run ->
            run.copy(cones = run.cones + 1)
        }
        resource.put(incrementedRun)
        return incrementedRun
    }

    fun decrementCones(eventId: UUID, id: UUID): RunDbEntity {
        val run = resource.getRun(eventId = eventId, id = id)
        check(run.cones >= 1) { "Run must have more than one cone, this one has ${run.cones}" }
        val decrementedRun = run.copy(cones = run.cones - 1)
        resource.put(decrementedRun)
        return decrementedRun
    }

    fun changeRerun(eventId: UUID, id: UUID, newValue: Boolean): RunDbEntity {
        val changedRun = resource.getRun(eventId = eventId, id = id)
                .copy(rerun = newValue)
        resource.put(changedRun)
        return changedRun
    }

    fun changeDriver(
            eventId: UUID,
            id: UUID,
            category: String,
            handicap: String,
            number: String
    ): RunDbEntity {
        val changedRun = resource.getRun(eventId = eventId, id = id)
                .copy(category = category, handicap = handicap, number = number)
        resource.put(changedRun)
        return changedRun
    }

    fun deleteRun(eventId: UUID, id: UUID) {
        val run = resource.getRun(eventId = eventId, id = id)
        val runsBySequence = listRuns(eventId)
                .sortedBy { it.sequence }
                .toMutableList()
        when (runsBySequence.removeIf { it.id == run.id }) {
            false -> return // TODO: error handling
        }
        val decrementedSequenceRuns = runsBySequence.filter { it.sequence > run.sequence }
                .map { it.copy(sequence = it.sequence - 1) }
        resource.delete(run)
        decrementedSequenceRuns.forEach { resource.put(it) }
    }

    fun alterDriverSequence(request: AlterDriverSequenceRequest): AlterDriverSequenceResult {
        val runs = listRuns(request.eventId)
                .sortedBy { it.sequence }
                .toMutableList()
        val insertSequence = when(request.relative) {
            AlterDriverSequenceRequest.Relative.BEFORE -> maxOf(request.sequence, 1)
            AlterDriverSequenceRequest.Relative.AFTER -> request.sequence + 1
        }
        val insertIndex = insertSequence - 1 // sequence is 1-indexed, lists are 0-indexed
        val insertRun = RunDbEntity(
                eventId = request.eventId,
                category = request.category,
                handicap = request.handicap,
                number = request.number,
                sequence = insertSequence,
                rawTime = runs.getOrNull(insertIndex)?.rawTime
        )
        runs.add(insertIndex, insertRun)
        val shiftRuns = runs.filterIndexed { index, run ->
            index >= insertIndex && run.id != insertRun.id
        }
        shiftRuns.mapIndexed { index, shiftRun ->
            val nextShiftRun = index + 1
            shiftRun.copy(
                    sequence = shiftRun.sequence + 1,
                    rawTime = shiftRuns.getOrNull(nextShiftRun)?.rawTime
            )
        }
        val result = AlterDriverSequenceResult(
                runs = runs,
                insertRunId = insertRun.id,
                shiftRunIds = shiftRuns.map { it.id }.toHashSet()
        )
        if (!request.dryRun) {
            resource.put(insertRun)
            shiftRuns.forEach { resource.put(it) }
        }
        return result
    }

    private fun RunDbEntity.hasDriver(): Boolean {
        return handicap.isNotBlank() && number.isNotBlank()
    }
}
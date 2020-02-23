package org.coner.drs.node.service

import assertk.all
import assertk.assertAll
import assertk.assertThat
import assertk.assertions.*
import org.coner.drs.node.db.DigitalRawSheetDatabase
import org.coner.drs.node.db.entity.EventDbEntity
import org.coner.drs.node.db.entity.RunDbEntity
import org.coner.drs.node.entity.*
import org.coner.drs.node.fixture.Events
import org.coner.drs.node.fixture.Registrations
import org.coner.drs.node.payload.*
import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.math.BigDecimal
import java.nio.file.Path

class RunServiceTest {

    @TempDir
    lateinit var tempDir: Path
    lateinit var database: DigitalRawSheetDatabase
    lateinit var service: RunService

    private lateinit var event: EventDbEntity
    private lateinit var registrations: List<Array<String>>

    @BeforeEach
    fun before() {
        database = DigitalRawSheetDatabase(tempDir)
        event = Events.basic()
        registrations = Registrations.basic()
        database.entity<EventDbEntity>().put(event)
        service = RunService(database)
    }

    @Test
    fun `it should find blank run with sequence 1 when no runs yet exist`() {
        val actual = service.findRunForNextTime(event.id)

        assertThat(actual).all {
            hasSequence(1)
            registrationIsEmpty()
        }
    }

    @Test
    fun `it should find first run when only runs without times exist`() {
        val registration = registrations[0]
        val run = RunDbEntity(
                eventId = event.id,
                sequence = 1,
                category = registration[0],
                handicap = registration[1],
                number = registration[2]
        )
        database.entity<RunDbEntity>().put(run)

        val actual = service.findRunForNextTime(event.id)

        assertThat(actual).all {
            sequenceIsEqualTo(run)
            registrationIsEqualTo(run)
        }
    }

    @Test
    fun `it should find second run when first run has time already`() {
        val runs = listOf(
                RunDbEntity(
                        eventId = event.id,
                        sequence = 1,
                        category = registrations[0][0],
                        handicap = registrations[0][1],
                        number = registrations[0][2],
                        rawTime = BigDecimal.valueOf(123456, 3)
                ),
                RunDbEntity(
                        eventId = event.id,
                        sequence = 2,
                        category = registrations[1][0],
                        handicap = registrations[1][1],
                        number = registrations[1][2],
                        rawTime = null
                )
        )
        runs.forEach { database.entity<RunDbEntity>().put(it) }

        val actual = service.findRunForNextTime(event.id)

        val expected = runs[1]
        assertThat(actual).all {
            sequenceIsEqualTo(expected)
            registrationIsEqualTo(expected)
        }
    }

    @Test
    fun `it should make up a run when all runs in sequence have times already`() {
        val registration = registrations[0]
        val run = RunDbEntity(
                eventId = event.id,
                sequence = 1,
                category = registration[0],
                handicap = registration[1],
                number = registration[2],
                rawTime = BigDecimal.valueOf(123456, 3)
        )
        database.entity<RunDbEntity>().put(run)

        val actual = service.findRunForNextTime(event.id)

        assertThat(actual).all {
            hasSequence(2)
            registrationIsEmpty()
        }
    }

    @Test
    fun `it should insert driver into sequence before given sequence`() {
        val runs = listOf(
                RunDbEntity(
                        eventId = event.id,
                        sequence = 1,
                        category = registrations[0][0],
                        handicap = registrations[0][1],
                        number = registrations[0][2],
                        rawTime = BigDecimal.valueOf(1000, 3)
                ),
                RunDbEntity(
                        eventId = event.id,
                        sequence = 2,
                        category = registrations[2][0],
                        handicap = registrations[2][1],
                        number = registrations[2][2],
                        rawTime = BigDecimal.valueOf(2000, 3)
                )
        )
        runs.forEach { database.entity<RunDbEntity>().put(it) }
        val request = AlterDriverSequenceRequest(
                eventId = event.id,
                sequence = 2,
                relative = AlterDriverSequenceRequest.Relative.BEFORE,
                category = registrations[1][0],
                handicap = registrations[1][1],
                number = registrations[1][2],
                dryRun = true
        )

        val actual = service.alterDriverSequence(request)

        assertThat(actual).all {
            hasRunsCount(3)
            runAt(0).all("0-index run not modified") {
                isDataClassEqualTo(runs[0])
            }
            insertedRunIdMatchesRunAtIndex(1)
            runAt(1).all("1-index run was inserted") {
                hasSequence(2)
                hasRegistration(registrations[1])
                rawTimeIsEqualTo(runs[1])
            }
            runAt(2).all("2-index run was shifted") {
                idIsEqualTo(runs[1])
                hasSequence(3)
                hasRegistration(registrations[2])
                hasRawTime(null)
            }
            shiftedRunIds { hasSize(1) }
            shiftedRunIdsContainsRunIdAtIndex(2)
        }
    }

    @Test
    fun `it should insert driver into sequence after given sequence`() {
        val runs = listOf(
                RunDbEntity(
                        eventId = event.id,
                        sequence = 1,
                        category = registrations[0][0],
                        handicap = registrations[0][1],
                        number = registrations[0][2],
                        rawTime = BigDecimal.valueOf(1000, 3)
                ),
                RunDbEntity(
                        eventId = event.id,
                        sequence = 2,
                        category = registrations[2][0],
                        handicap = registrations[2][1],
                        number = registrations[2][2],
                        rawTime = BigDecimal.valueOf(2000, 3)
                )
        )
        runs.forEach { database.entity<RunDbEntity>().put(it) }
        val request = AlterDriverSequenceRequest(
                eventId = event.id,
                sequence = 1,
                relative = AlterDriverSequenceRequest.Relative.AFTER,
                category = registrations[1][0],
                handicap = registrations[1][1],
                number = registrations[1][2],
                dryRun = true
        )

        val actual = service.alterDriverSequence(request)

        assertThat(actual).all {
            hasRunsCount(3)
            runAt(0).all("0-index run not modified") {
                isDataClassEqualTo(runs[0])
            }
            insertedRunIdMatchesRunAtIndex(1)
            runAt(1).all("1-index run was inserted") {
                hasSequence(2)
                hasRegistration(registrations[1])
                rawTimeIsEqualTo(runs[1])
            }
            shiftedRunIdsContainsRunIdAtIndex(2)
            shiftedRunIds { hasSize(1) }
            runAt(2).all("2-index run was shifted") {
                hasSequence(3)
                hasRegistration(registrations[2])
                hasRawTime(null)
            }
        }
    }

    @Test
    fun `it should not write to disk on alter driver sequence dry run`() {
        val runs = listOf(
                RunDbEntity(
                        eventId = event.id,
                        sequence = 1,
                        category = registrations[1][0],
                        handicap = registrations[1][1],
                        number = registrations[1][2]
                )
        )
        runs.forEach { database.entity<RunDbEntity>().put(it) }
        val request = AlterDriverSequenceRequest(
                eventId = event.id,
                sequence = 1,
                relative = AlterDriverSequenceRequest.Relative.BEFORE,
                category = registrations[0][0],
                handicap = registrations[0][1],
                number = registrations[0][2],
                dryRun = true
        )

        val actualResult = service.alterDriverSequence(request)
        val actualRunsOnDisk = service.listRuns(event.id)

        assertAll {
            assertThat(actualResult).hasRunsCount(2)
            assertThat(actualRunsOnDisk).hasSize(1)
        }
    }

    @Test
    fun `it should write to disk on alter driver sequence`() {
        val runs = listOf(
                RunDbEntity(
                        eventId = event.id,
                        sequence = 1,
                        category = registrations[1][0],
                        handicap = registrations[1][1],
                        number = registrations[1][2]
                )
        )
        runs.forEach { database.entity<RunDbEntity>().put(it) }
        val request = AlterDriverSequenceRequest(
                eventId = event.id,
                sequence = 1,
                relative = AlterDriverSequenceRequest.Relative.BEFORE,
                category = registrations[0][0],
                handicap = registrations[0][1],
                number = registrations[0][2],
                dryRun = false
        )

        val actualResult = service.alterDriverSequence(request)
        val actualRunsOnDisk = service.listRuns(event.id)

        assertThat(actualRunsOnDisk).all {
            hasSize(2)
        }
    }

    @Test
    fun `It should clear time from run`() {
        val run = RunDbEntity(
                eventId = event.id,
                sequence = 1,
                category = registrations[0][0],
                handicap = registrations[0][1],
                number = registrations[0][2],
                rawTime = BigDecimal.valueOf(123456, 3)
        )
        database.entity<RunDbEntity>().put(run)

        val actualRun = service.changeTime(eventId = event.id, id = run.id, rawTime = null)
        val actualRunOnDisk = service.findRunById(eventId = event.id, id = run.id)

        assertThat(actualRunOnDisk).all {
            hasRawTime(null)
            isEqualTo(actualRun)
        }
    }
}
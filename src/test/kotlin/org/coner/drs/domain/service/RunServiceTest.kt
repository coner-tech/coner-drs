package org.coner.drs.domain.service

import assertk.all
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import assertk.assertions.isSameAs
import assertk.assertions.prop
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assumptions
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.test.TornadoFxScopeExtension
import org.coner.drs.test.fixture.domain.entity.RunEvents
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxToolkit
import tornadofx.*
import java.math.BigDecimal

@ExtendWith(TornadoFxScopeExtension::class)
class RunServiceTest {

    private lateinit var service: RunService

    @BeforeEach
    fun before(scope: Scope) {
        service = find(scope)
    }

    @Test
    fun `it should find blank run with sequence 1 when no runs yet exist`() {
        val runEvent = RunEvents.basic()
        Assumptions.assumeThat(runEvent.runsBySequence).isEmpty()

        val actual = service.findRunForNextTime(runEvent)

        assertk.assertThat(actual).all {
            prop(Run::sequence).isEqualTo(1)
            prop(Run::registration).isNull()
        }
    }

    @Test
    fun `it should find first run when only runs without times exist`() {
        val runEvent = RunEvents.basic()
        Assumptions.assumeThat(runEvent.runsBySequence).isEmpty()
        runEvent.runs += Run(
                sequence = 1,
                registration = runEvent.registrations[0],
                event = runEvent,
                rawTime = null
        )

        val actual = service.findRunForNextTime(runEvent)

        assertk.assertThat(actual).all {
            prop(Run::sequence).isEqualTo(1)
            prop(Run::registration).isSameAs(runEvent.registrations[0])
        }
    }

    @Test
    fun `it should find second run when first run has time already`() {
        val runEvent = RunEvents.basic()
        Assumptions.assumeThat(runEvent.runsBySequence).isEmpty()
        runEvent.runs += Run(
                sequence = 1,
                registration = runEvent.registrations[0],
                event = runEvent,
                rawTime = BigDecimal.valueOf(123456, 3)
        )
        runEvent.runs += Run(
                sequence = 2,
                registration = runEvent.registrations[1],
                event = runEvent,
                rawTime = null
        )

        val actual = service.findRunForNextTime(runEvent)

        assertk.assertThat(actual).all {
            prop(Run::sequence).isEqualTo(2)
            prop(Run::registration).isSameAs(runEvent.registrations[1])
        }
    }

    @Test
    fun `it should make up a run when all runs in sequence have times already`() {
        val runEvent = RunEvents.basic()
        Assumptions.assumeThat(runEvent.runsBySequence).isEmpty()
        runEvent.runs += Run(
                sequence = 1,
                registration = runEvent.registrations[0],
                event = runEvent,
                rawTime = BigDecimal.valueOf(123456, 3)
        )

        val actual = service.findRunForNextTime(runEvent)

        assertk.assertThat(actual).all {
            prop(Run::sequence).isEqualTo(2)
            prop(Run::registration).isNull()
        }
    }
}
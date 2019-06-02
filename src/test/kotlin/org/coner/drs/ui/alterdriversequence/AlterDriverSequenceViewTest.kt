package org.coner.drs.ui.alterdriversequence

import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.reactivex.Single
import javafx.stage.Stage
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assumptions
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
import org.coner.drs.domain.payload.InsertDriverIntoSequenceResult
import org.coner.drs.domain.service.RunService
import org.coner.drs.test.extension.Init
import org.coner.drs.test.extension.TornadoFxViewExtension
import org.coner.drs.test.extension.View
import org.coner.drs.test.fixture.domain.entity.RunEvents
import org.coner.drs.test.page.fast.FastAlterDriverSequencePage
import org.coner.drs.test.page.real.RealAlterDriverSequencePage
import org.coner.drs.ui.runevent.RunEventModel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import tornadofx.*

@ExtendWith(TornadoFxViewExtension::class, MockKExtension::class)
internal class AlterDriverSequenceViewTest {

    @View
    private lateinit var view: AlterDriverSequenceView
    private lateinit var model: AlterDriverSequenceModel
    private lateinit var controller: AlterDriverSequenceController
    private lateinit var runEventModel: RunEventModel

    private lateinit var realPage: RealAlterDriverSequencePage
    private lateinit var fastPage: FastAlterDriverSequencePage

    @RelaxedMockK
    lateinit var runService: RunService

    @Init
    fun init(scope: Scope) {
        scope.apply {
            set(runService)
        }
        runEventModel = find(scope)
        runEventModel.event = RunEvents.basic()
        view = find(scope)
        model = find(scope)
        controller = find(scope)
        model.apply {
            event = runEventModel.event
            sequence = 1
            relative = InsertDriverIntoSequenceRequest.Relative.BEFORE
        }
    }

    @BeforeEach
    fun beforeEach(robot: FxRobot) {
        realPage = RealAlterDriverSequencePage(robot)
        fastPage = FastAlterDriverSequencePage(robot)
    }

    @Test
    fun `It should execute alter driver sequence when user clicks OK`(robot: FxRobot, stage: Stage) {
        Assumptions.assumeThat(model.result).isNull()
        val result: InsertDriverIntoSequenceResult = mockk()
        every {
            runService.insertDriverIntoSequence(any())
        }.returns(Single.just(result))

        realPage.clickOkButton()

        Assertions.assertThat(model.result).isSameAs(result)
        Assertions.assertThat(stage.isShowing).isFalse()
    }
}
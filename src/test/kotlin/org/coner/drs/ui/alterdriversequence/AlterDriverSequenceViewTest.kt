package org.coner.drs.ui.alterdriversequence

import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.reactivex.Single
import javafx.stage.Stage
import me.carltonwhitehead.tornadofx.junit5.Init
import me.carltonwhitehead.tornadofx.junit5.TornadoFxViewExtension
import me.carltonwhitehead.tornadofx.junit5.View
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assumptions
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
import org.coner.drs.domain.payload.InsertDriverIntoSequenceResult
import org.coner.drs.domain.service.RunService
import org.coner.drs.test.fixture.domain.entity.RunEvents
import org.coner.drs.test.page.fast.FastAlterDriverSequencePage
import org.coner.drs.test.page.real.RealAlterDriverSequencePage
import org.coner.drs.ui.runevent.RunEventModel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import tornadofx.*
import java.util.*
import java.util.concurrent.CountDownLatch

@ExtendWith(TornadoFxViewExtension::class, MockKExtension::class)
internal class AlterDriverSequenceViewTest {

    @View
    private lateinit var view: AlterDriverSequenceView
    private lateinit var model: AlterDriverSequenceModel
    private lateinit var controller: AlterDriverSequenceController

    private lateinit var realPage: RealAlterDriverSequencePage
    private lateinit var fastPage: FastAlterDriverSequencePage

    @RelaxedMockK
    lateinit var runService: RunService

    @Init
    fun init(scope: Scope) {
        scope.apply {
            set(runService)
        }
        model = find<AlterDriverSequenceModel>(scope).apply {
            event = RunEvents.basic()
            sequence = 1
            relative = InsertDriverIntoSequenceRequest.Relative.BEFORE
        }
        controller = find(scope)
        view = find(scope)
    }

    @BeforeEach
    fun beforeEach(robot: FxRobot) {
        realPage = RealAlterDriverSequencePage(robot)
        fastPage = FastAlterDriverSequencePage(robot)
    }

    @Test
    fun `It should display preview result runs`() {
        val runsTable = fastPage.toPreviewAlteredDriverSequencePage().runsTable()
        Assumptions.assumeThat(runsTable.items).isNullOrEmpty()

        FX.runAndWait {
            val insertedRunId = UUID.randomUUID()
            model.previewResult = InsertDriverIntoSequenceResult(
                    runs = model.event.runs.apply {
                        add(Run(id = insertedRunId, event = model.event, sequence = 1))
                    },
                    shiftRunIds = emptySet(),
                    insertRunId = insertedRunId
            )
        }

        Assertions.assertThat(runsTable.items).hasSize(1)
    }

    @Test
    fun `It should select inserted run in preview`() {
        val runsTable = fastPage.toPreviewAlteredDriverSequencePage().runsTable()

        FX.runAndWait {
            val insertedRunId = UUID.randomUUID()
            model.previewResult = InsertDriverIntoSequenceResult(
                    runs = model.event.runs.apply {
                        add(Run(id = insertedRunId, event = model.event, sequence = 1))
                    },
                    shiftRunIds = emptySet(),
                    insertRunId = insertedRunId
            )
        }

        Assertions.assertThat(runsTable.selectionModel.selectedIndex).isEqualTo(0)
    }

    @Test
    fun `It should execute alter driver sequence when user clicks OK`(stage: Stage) {
        Assumptions.assumeThat(model.result).isNull()
        val result: InsertDriverIntoSequenceResult = mockk()
        val latch = CountDownLatch(1)
        every {
            runService.insertDriverIntoSequence(any())
        }.returns(Single.just(result))
        model.resultProperty.onChangeOnce { latch.countDown() }

        realPage.clickOkButton()

        latch.await()
        Assertions.assertThat(model.result).isSameAs(result)
        Assertions.assertThat(stage.isShowing).isFalse()
    }

    @Test
    fun `Alter driver sequence window should open with numbers field focused`() {
        val numbersField = fastPage.toSpecifyDriverSequenceAlterationPage().numbersField()
        Assertions.assertThat(numbersField.isFocused).isTrue()
    }
}

private fun basicPreviewResult(event: RunEvent): PreviewAlteredDriverSequenceResult {
    val inserted = PreviewAlteredDriverSequenceResult.Run(
            run = Run(
                    event = event,
                    sequence = 1,
                    registration = event.registrations[0]
            ),
            status = PreviewAlteredDriverSequenceResult.Status.INSERTED
    )

    return PreviewAlteredDriverSequenceResult(
            runs = observableListOf(
                    inserted,
                    PreviewAlteredDriverSequenceResult.Run(
                            run = Run(
                                    event = event,
                                    sequence = 2,
                                    registration = event.registrations[1]
                            ),
                            status = PreviewAlteredDriverSequenceResult.Status.SHIFTED
                    )
            ),
            insertedRun = inserted
    )
}
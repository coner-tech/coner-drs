package org.coner.drs.ui.alterdriversequence

import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.test.extension.Init
import org.coner.drs.test.extension.TornadoFxViewExtension
import org.coner.drs.test.extension.View
import org.coner.drs.test.fixture.domain.entity.RunEvents
import org.coner.drs.test.page.fast.FastPreviewAlteredDriverSequencePage
import org.coner.drs.test.page.real.RealPreviewAlteredDriverSequencePage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import tornadofx.*

@ExtendWith(TornadoFxViewExtension::class, MockKExtension::class)
class PreviewAlteredDriverSequenceViewTest {

    @View
    private lateinit var view: PreviewAlteredDriverSequenceView
    private lateinit var model: PreviewAlteredDriverSequenceModel
    private lateinit var controller: PreviewAlteredDriverSequenceController

    private lateinit var realPage: RealPreviewAlteredDriverSequencePage
    private lateinit var fastPage: FastPreviewAlteredDriverSequencePage

    @RelaxedMockK
    private lateinit var alterDriverSequenceModel: AlterDriverSequenceModel

    @Init
    fun init(scope: Scope) {
        scope.apply {
            set(alterDriverSequenceModel)
        }
        view = find(scope)
        model = find(scope)
        controller = find(scope)
    }

    @BeforeEach
    fun beforeEach(robot: FxRobot) {
        realPage = RealPreviewAlteredDriverSequencePage(robot)
        fastPage = FastPreviewAlteredDriverSequencePage(robot)
    }

    @Test
    fun `It should display preview result runs`() {
        val event = RunEvents.basic()

        model.previewResult = basicPreviewResult(event)

        val runsTable = fastPage.runsTable()
        assertThat(runsTable.items).hasSize(2)
    }

    @Test
    fun `It should select inserted run`() {
        val event = RunEvents.basic()

        model.previewResult = basicPreviewResult(event)

        val runsTable = fastPage.runsTable()
        assertThat(runsTable.selectionModel.selectedIndex).isEqualTo(0)
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
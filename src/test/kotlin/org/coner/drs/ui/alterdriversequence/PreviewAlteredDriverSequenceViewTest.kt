package org.coner.drs.ui.alterdriversequence

import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Condition
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.test.extension.Init
import org.coner.drs.test.extension.TornadoFxViewExtension
import org.coner.drs.test.extension.View
import org.coner.drs.test.fixture.domain.entity.RunEvents
import org.junit.jupiter.api.Assertions.*
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

    @Test
    fun `It should display preview result runs`(robot: FxRobot) {
        val event = RunEvents.basic()

        model.previewResult = basicPreviewResult(event)

        val runsTable = robot.lookup("#runs-table").queryTableView<PreviewAlteredDriverSequenceResult.Run>()
        assertThat(runsTable.items).hasSize(2)
    }

    @Test
    fun `It should select inserted run`(robot: FxRobot) {
        val event = RunEvents.basic()

        model.previewResult = basicPreviewResult(event)

        val runsTable = robot.lookup("#runs-table").queryTableView<PreviewAlteredDriverSequenceResult.Run>()
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
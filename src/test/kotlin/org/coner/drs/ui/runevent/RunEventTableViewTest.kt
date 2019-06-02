package org.coner.drs.ui.runevent

import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.payload.InsertDriverIntoSequenceResult
import org.coner.drs.test.extension.Init
import org.coner.drs.test.extension.TornadoFxViewExtension
import org.coner.drs.test.extension.View
import org.coner.drs.test.fixture.domain.entity.RunEvents
import org.coner.drs.test.page.fast.FastRunEventTablePage
import org.coner.drs.test.page.real.RealRunEventTablePage
import org.coner.drs.ui.alterdriversequence.AlterDriverSequenceController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import org.testfx.assertions.api.Assertions
import tornadofx.*

@ExtendWith(TornadoFxViewExtension::class, MockKExtension::class)
class RunEventTableViewTest {

    @View
    private lateinit var view: RunEventTableView
    private lateinit var model: RunEventTableModel
    private lateinit var controller: RunEventTableController

    private lateinit var realPage: RealRunEventTablePage
    private lateinit var fastPage: FastRunEventTablePage

    @Init
    fun init(scope: Scope) {
        find<RunEventModel>(scope).apply {
            event = RunEvents.basic()
        }
        controller = find(scope)
        model = find(scope)
        view = find(scope)
    }

    @BeforeEach
    fun beforeEach(robot: FxRobot) {
        realPage = RealRunEventTablePage(robot)
        fastPage = FastRunEventTablePage(robot)
    }

    @Test
    fun `When user inserts driver into sequence it should select it`() {
        val event = find<RunEventModel>().event
        FX.runAndWait {
            event.runs += Run(
                    event = event,
                    sequence = 1,
                    registration = event.registrations[0]
            )
        }
        val alterDriverSequenceController: AlterDriverSequenceController = mockk(relaxed = true)
        val insertRun = Run(
                event = event,
                sequence = 1,
                registration = event.registrations[1]
        )
        every {
            alterDriverSequenceController.showAlterDriverSequenceAndWait(event.runs[0].sequence)
        }.answers {
            val runs = listOf(insertRun, event.runs[0])
            event.runs[0].sequence = 2
            event.runs.setAll(runs)
            InsertDriverIntoSequenceResult(
                    runs = runs,
                    insertRunId = insertRun.id,
                    shiftRunIds = hashSetOf(event.runs[0].id)
            )
        }
        setInScope(alterDriverSequenceController)
        val runsTable = fastPage.runsTable()
        val sequence = event.runs[0].sequence
        fastPage.selectSequence(sequence)

        realPage.keyboardShortcutInsertDriverIntoSequence(sequence)

        Assertions.assertThat(runsTable.selectionModel.selectedItem).isSameAs(insertRun)
    }
}
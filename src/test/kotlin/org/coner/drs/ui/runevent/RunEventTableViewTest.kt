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
import org.coner.drs.ui.alterdriversequence.AlterDriverSequenceController
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

    @Init
    fun init(scope: Scope) {
        find<RunEventModel>(scope).apply {
            event = RunEvents.basic()
        }
        controller = find(scope)
        model = find(scope)
        view = find(scope)
    }

    @Test
    fun `When user inserts driver into sequence it should select it`(robot: FxRobot) {
        val event = find<RunEventModel>().event
        robot.interact {
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
        val runsTable = robot.lookup("#runs-table").queryTableView<Run>()

        robot.rightClickOn(event.registrations[0].numbers)
        robot.clickOn("Driver")
        robot.clickOn("Insert Driver Into Sequence")

        Assertions.assertThat(runsTable.selectionModel.selectedItem).isSameAs(insertRun)
    }
}
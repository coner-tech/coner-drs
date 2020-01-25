/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
import org.awaitility.Awaitility.await
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
import org.coner.drs.domain.payload.InsertDriverIntoSequenceResult
import org.coner.drs.domain.service.RunService
import org.coner.drs.test.fixture.domain.entity.RunEvents
import org.coner.drs.test.page.fast.FastAlterDriverSequencePage
import org.coner.drs.test.page.real.RealAlterDriverSequencePage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import tornadofx.*
import java.util.*

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
        every {
            runService.insertDriverIntoSequence(any())
        }.returns(Single.just(result))

        realPage.clickOkButton()

        await().until { model.result != null }
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
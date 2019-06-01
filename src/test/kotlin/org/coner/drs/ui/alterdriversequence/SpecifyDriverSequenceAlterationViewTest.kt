package org.coner.drs.ui.alterdriversequence

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import javafx.scene.input.KeyCode
import org.assertj.core.api.Assumptions
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
import org.coner.drs.domain.payload.RegistrationSelectionCandidate
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.domain.service.RunService
import org.coner.drs.test.extension.Init
import org.coner.drs.test.extension.TornadoFxViewExtension
import org.coner.drs.test.extension.View
import org.coner.drs.test.fixture.domain.entity.RunEvents
import org.coner.drs.test.page.fast.FastSpecifyDriverSequenceAlterationPage
import org.coner.drs.test.page.real.RealSpecifyDriverSequenceAlterationPage
import org.coner.drs.ui.runevent.RunEventModel
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import org.testfx.assertions.api.Assertions
import tornadofx.*

@ExtendWith(TornadoFxViewExtension::class, MockKExtension::class)
internal class SpecifyDriverSequenceAlterationViewTest {

    @View
    private lateinit var view: SpecifyDriverSequenceAlterationView

    private lateinit var model: SpecifyDriverSequenceAlterationModel
    private lateinit var controller: SpecifyDriverSequenceAlterationController

    private lateinit var realPage: RealSpecifyDriverSequenceAlterationPage
    private lateinit var fastPage: FastSpecifyDriverSequenceAlterationPage

    @RelaxedMockK
    private lateinit var registrationService: RegistrationService
    @RelaxedMockK
    private lateinit var runService: RunService

    @Init
    fun init(scope: Scope) {
        scope.apply {
            set(registrationService)
            set(runService)
            set(find<RunEventModel>().apply {
                event = RunEvents.basic()
            })
        }
        view = find(scope)
        model = find(scope)
        controller = find(scope)
    }

    @BeforeEach
    fun beforeEach(robot: FxRobot) {
        realPage = RealSpecifyDriverSequenceAlterationPage(robot)
        fastPage = FastSpecifyDriverSequenceAlterationPage(robot)
    }

    @Test
    fun `Numbers field is bound to model property`() {
        Assumptions.assumeThat(model.numbersField).isNullOrEmpty()
        val numbers = "8 STR"

        realPage.writeInNumbersField(numbers)

        assertThat(model.numbersField).isEqualTo(numbers)
    }

    @Test
    fun `When numbers field focused, vertical arrow keys should affect registration list selection`(robot: FxRobot) {
        fastPage.focusNumbersField()
        Assumptions.assumeThat(fastPage.numbersField().isFocused).isTrue()
        Assumptions.assumeThat(fastPage.registrationsListView().selectionModel.selectedIndex).isEqualTo(-1)

        fun typeAndAssert(keyCode: KeyCode, index: Int) {
            robot.type(keyCode)
            Assertions.assertThat(realPage.registrationsListView().selectionModel.selectedIndex).isEqualTo(index)
        }

        typeAndAssert(KeyCode.DOWN, 0)
        typeAndAssert(KeyCode.DOWN, 1)
        typeAndAssert(KeyCode.UP, 0)
        typeAndAssert(KeyCode.UP, 0)
    }

    @Test
    fun `Model registration should contain registration selected in list`() {
        val registration = fastPage.registrationsListView().items[0]

        realPage.selectRegistration(registration)

        Assertions.assertThat(model.registration).isSameAs(registration)
    }

    @Test
    fun `Model relative should match form selection`() {
        val before = InsertDriverIntoSequenceRequest.Relative.BEFORE
        val after = InsertDriverIntoSequenceRequest.Relative.AFTER
        Assumptions.assumeThat(model.relative).isEqualTo(before)

        realPage.selectRelativeAfter()
        Assertions.assertThat(model.relative).isEqualTo(after)

        realPage.selectRelativeBefore()
        Assertions.assertThat(model.relative).isEqualTo(before)
    }

    @Test
    fun `Model registration should take auto selection candidate`() {
        val registration = find<RunEventModel>().event.registrations[2]
        every {
            registrationService.findAutoSelectionCandidate(model.registrationList, registration.numbers)
        }.returns(RegistrationSelectionCandidate(registration, 0))

        realPage.writeInNumbersField(registration.numbers)

        Assertions.assertThat(model.registrationListAutoSelectionCandidate).isNotNull
        Assertions.assertThat(model.registrationListAutoSelectionCandidate.registration).isSameAs(registration)
        Assertions.assertThat(model.registration).isSameAs(registration)
    }

}
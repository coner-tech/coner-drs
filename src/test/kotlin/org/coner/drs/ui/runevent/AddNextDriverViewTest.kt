package org.coner.drs.ui.runevent

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import javafx.scene.input.KeyCode
import org.assertj.core.api.Assumptions
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.domain.service.RunService
import org.coner.drs.test.extension.Init
import org.coner.drs.test.extension.TornadoFxViewExtension
import org.coner.drs.test.extension.View
import org.coner.drs.test.fixture.domain.entity.RunEvents
import org.coner.drs.test.page.AddNextDriverPage
import org.coner.drs.test.page.fast.FastAddNextDriverPage
import org.coner.drs.test.page.real.RealAddNextDriverPage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import org.testfx.assertions.api.Assertions
import tornadofx.*

@ExtendWith(TornadoFxViewExtension::class, MockKExtension::class)
class AddNextDriverViewTest {

    @View
    private lateinit var view: AddNextDriverView
    private lateinit var model: AddNextDriverModel
    private lateinit var controller: AddNextDriverController

    private lateinit var runEventModel: RunEventModel

    private lateinit var realPage: AddNextDriverPage
    private lateinit var fastPage: AddNextDriverPage

    @RelaxedMockK
    private lateinit var registrationService: RegistrationService
    @RelaxedMockK
    private lateinit var runService: RunService

    @Init
    fun init(scope: Scope) {
        scope.apply {
            set(registrationService)
            set(runService)
        }
        runEventModel = find<RunEventModel>(scope).apply {
            event = RunEvents.basic()
        }
        view = find(scope)
        model = find(scope)
        controller = find(scope)
    }

    @BeforeEach
    fun beforeEach(robot: FxRobot) {
        realPage = RealAddNextDriverPage(robot)
        fastPage = FastAddNextDriverPage(robot)
    }

    @Test
    fun `Numbers field is bound to model property`(robot: FxRobot) {
        Assumptions.assumeThat(model.numbersField).isNullOrEmpty()
        val numbers = "8 STR"

        realPage.writeInNumbersField(numbers)

        assertThat(model.numbersField).isEqualTo(numbers)
    }

    @Test
    fun `Alt+N focuses Numbers field`() {
        Assumptions.assumeThat(fastPage.numbersField().isFocused).isFalse()

        realPage.focusNumbersField()

        Assertions.assertThat(realPage.numbersField()).isFocused
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
    fun `When registration selected, it should be able to add next driver`(robot: FxRobot) {
        val registration = fastPage.registrationsListView().items[0]
        fastPage.selectRegistration(registration)

        realPage.doAddSelectedRegistration()

        verify { runService.addNextDriver(runEventModel.event, registration) }
    }

    @Test
    fun `When numbers field filled arbitrarily, it should be able to add exact numbers`(robot: FxRobot) {
        val registration = Registration(
                number = "123",
                handicap = "ABC",
                category = ""
        )
        val numbersFieldTokens = listOf("123", "ABC")
        every { registrationService.findNumbersFieldTokens("123 ABC") }.returns(numbersFieldTokens)
        every { registrationService.findNumbersFieldArbitraryRegistration(numbersFieldTokens) }.returns(registration)
        every { registrationService.findNumbersFieldContainsNumbersTokens(numbersFieldTokens) }.returns(true)
        fastPage.writeInNumbersField("123 ABC")

        realPage.doAddForceExactNumbers()

        verify { runService.addNextDriver(runEventModel.event, registration) }
    }

}
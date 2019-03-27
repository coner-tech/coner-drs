package org.coner.drs.ui.runevent

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import org.assertj.core.api.Assumptions
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.domain.service.RunService
import org.coner.drs.test.TornadoFxScopeExtension
import org.coner.drs.test.fixture.domain.entity.RunEvents
import org.coner.drs.test.page.AddNextDriverPage
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.api.FxRobot
import org.testfx.assertions.api.Assertions
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.Init
import org.testfx.framework.junit5.Start
import tornadofx.*

@ExtendWith(TornadoFxScopeExtension::class, ApplicationExtension::class, MockKExtension::class)
class AddNextDriverViewTest {

    companion object {
        private lateinit var scope: Scope

        @JvmStatic
        @BeforeAll
        fun beforeAll(scope: Scope) {
            this.scope = scope
        }

    }

    private lateinit var view: AddNextDriverView
    private lateinit var page: AddNextDriverPage

    @RelaxedMockK
    private lateinit var registrationService: RegistrationService
    @RelaxedMockK
    private lateinit var runService: RunService

    fun prepareScope() {
        MockKAnnotations.init(this)
        scope = scope.apply {
            set(registrationService)
            set(runService)
        }
        find<RunEventModel>(scope).apply {
            event = RunEvents.basic()
        }
    }

    @Init
    fun init() {
        prepareScope()
        view = find(scope)
    }

    @Start
    fun start(stage: Stage) {
        stage.scene = Scene(view.root)
        stage.show()
    }

    @BeforeEach
    fun beforeEach(robot: FxRobot) {
        this.page = AddNextDriverPage(robot)
    }

    @Test
    fun `Numbers field is bound to model property`(robot: FxRobot) {
        robot.clickOn(page.numbersField())
        robot.write("8 STR")

        assertThat(find<AddNextDriverModel>(scope).numbersField).isEqualTo("8 STR")
    }

    @Test
    fun `Alt+N focuses Numbers field`() {
        Assumptions.assumeThat(page.numbersField().isFocused).isFalse()

        page.doNumbersFieldFocusKeyboardShortcut()

        Assertions.assertThat(page.numbersField()).isFocused
    }

    @Test
    fun `When numbers field focused, vertical arrow keys should affect registration list selection`(robot: FxRobot) {
        robot.interact {
            page.numbersField().requestFocus()
        }
        Assumptions.assumeThat(page.numbersField().isFocused).isTrue()
        Assumptions.assumeThat(page.registrationsListView().selectionModel.selectedIndex).isEqualTo(-1)

        fun typeAndAssert(keyCode: KeyCode, index: Int) {
            robot.type(keyCode)
            Assertions.assertThat(page.registrationsListView().selectionModel.selectedIndex).isEqualTo(index)
        }

        typeAndAssert(KeyCode.DOWN, 0)
        typeAndAssert(KeyCode.DOWN, 1)
        typeAndAssert(KeyCode.UP, 0)
        typeAndAssert(KeyCode.UP, 0)
    }

    @Test
    fun `When registration selected, it should be able to add next driver`(robot: FxRobot) {
        val registration = page.registrationsListView().items[0]
        robot.interact {
            page.registrationsListView().selectionModel.select(registration)
        }

        robot.clickOn(page.addButton())

        verify { runService.addNextDriver(find<RunEventModel>(scope).event, registration) }
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
        robot.interact {
            find<AddNextDriverModel>(scope).numbersField = "123 ABC"
        }

        page.doAddExactNumbersKeyboardShortcut()

        verify { runService.addNextDriver(find<RunEventModel>(scope).event, registration) }
    }

}
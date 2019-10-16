package org.coner.drs.it

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import javafx.scene.input.KeyCode
import me.carltonwhitehead.tornadofx.junit5.*
import me.carltonwhitehead.tornadofx.junit5.App
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assumptions
import org.assertj.core.groups.Tuple
import org.awaitility.Awaitility.await
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.TimerConfiguration
import org.coner.drs.io.gateway.EventGateway
import org.coner.drs.it.util.FilesystemFixture
import org.coner.drs.it.util.IntegrationTestApp
import org.coner.drs.test.fixture.integration.crispyfish.classdefinition.Thscc2019V0Classes
import org.coner.drs.test.fixture.integration.crispyfish.event.Thscc2019Points1
import org.coner.drs.test.page.*
import org.coner.drs.test.page.fast.FastAddNextDriverPage
import org.coner.drs.test.page.fast.FastChooseEventPage
import org.coner.drs.test.page.fast.FastStartPage
import org.coner.drs.test.page.real.RealRunEventPage
import org.coner.drs.ui.chooseevent.ChooseEventModel
import org.coner.drs.ui.chooseevent.ChooseEventTableView
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import org.testfx.api.FxRobot
import tornadofx.*
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExtendWith(TornadoFxAppExtension::class)
class RunEventIntegrationTest {

    @TempDir
    lateinit var tempDir: Path

    @App
    lateinit var app: tornadofx.App
    lateinit var folders: FilesystemFixture
    lateinit var event: Event
    lateinit var page: RunEventPage
    lateinit var addNextDriverPage: AddNextDriverPage
    lateinit var tablePage: RunEventTablePage
    lateinit var rightDrawerPage: RunEventRightDrawerPage
    lateinit var timerPage: TimerPage

    @Init
    fun init() {
        folders = FilesystemFixture(tempDir)
    }

    @SetupApp
    fun setupApp() = IntegrationTestApp(folders.appConfigBasePath)

    @Start
    fun start(robot: FxRobot) {
        println(folders.root)
        val startPage = FastStartPage(find(app.scope), robot)
        startPage.setRawSheetDatabase(folders.rawSheetDatabase)
        startPage.setCrispyFishDatabase(folders.crispyFishDatabase.toFile())
        startPage.clickStartButton()
        val classDefinitionFile = Thscc2019V0Classes.produceClassDefinitionFile(folders.crispyFishDatabase.toFile())
        val eventControlFile = Thscc2019Points1.produceEventControlFile(folders.crispyFishDatabase.toFile(), classDefinitionFile)
        val event = Event(
                date = LocalDate.parse("2019-03-03"),
                name = "RunEventIntegrationTest",
                crispyFishMetadata = Event.CrispyFishMetadata(
                        classDefinitionFile = classDefinitionFile.file,
                        eventControlFile = eventControlFile.file
                )
        )
        val fastChooseEventPage = FastChooseEventPage(robot)
        FX.runAndWait { find<EventGateway>(app.scope).save(event) }

        await("startup: choose event docked").until { find<ChooseEventModel>(app.scope).docked }
        await("startup: choose event table populated").until { find<ChooseEventTableView>(app.scope).root.items.isNotEmpty() }

        fastChooseEventPage.selectEvent { it.id == event.id }
        fastChooseEventPage.clickStartButton()
        this.event = find<EventGateway>(app.scope).list().first()
        this.page = RealRunEventPage(robot)
        this.addNextDriverPage = page.toAddNextDriverPage()
        this.tablePage = page.toTablePage()
        this.rightDrawerPage = page.toRightDrawerPage()
        this.timerPage = rightDrawerPage.toTimerPage()
    }

    @Test
    fun itShouldDisplayEvent() {
        assertThat(page.root().text).isEqualTo(event.name)
    }

    @Test
    fun itShouldAddNextDriver() {
        Assumptions.assumeThat(tablePage.runsTable().items).hasSize(0)

        addNextDriverPage.writeInNumbersField("1 HS")
        addNextDriverPage.doAddSelectedRegistration()

        assertThat(tablePage.runsTable()).all {
            this.prop("items") { it.items }.all {
                hasSize(1)
                index(0).all {
                    prop(Run::registrationNumbers).isEqualTo("1 HS")
                }
            }
        }
    }

    @Test
    fun itShouldInsertDriverIntoSequence(robot: FxRobot) {
        val runsTableItems = tablePage.runsTable().items
        Assumptions.assumeThat(runsTableItems).hasSize(0)
        addNextDriverPage.writeInNumbersField("1 HS")
        addNextDriverPage.doAddSelectedRegistration()


        val alterDriverSequencePage = tablePage.clickInsertDriverIntoSequence(1)
        val specifyDriverSequenceAlterationPage = alterDriverSequencePage.toSpecifyDriverSequenceAlterationPage()
        specifyDriverSequenceAlterationPage.writeInNumbersField("3 SSC")
        alterDriverSequencePage.clickOkButton()

        Assertions.assertThat(runsTableItems)
                .hasSize(2)
                .extracting("sequence", "registrationNumbers")
                .containsExactly(
                        Tuple(2, "1 HS"),
                        Tuple(1, "3 SSC")
                )
    }

    @Test
    fun itShouldIncrementAddNextDriverSequenceWhenInsertDriverIntoSequence(robot: FxRobot) {
        arrayOf("1 HS", "9 SM").forEach {
            addNextDriverPage.writeInNumbersField(it)
            addNextDriverPage.doAddSelectedRegistration()
        }
        val runsTableItems = tablePage.runsTable().items

        val alterDriverSequencePage = tablePage.clickInsertDriverIntoSequence(2)
        val specifyDriverSequenceAlterationPage = alterDriverSequencePage.toSpecifyDriverSequenceAlterationPage()
        specifyDriverSequenceAlterationPage.writeInNumbersField("33 SM")
        alterDriverSequencePage.clickOkButton()

        Assertions.assertThat(addNextDriverPage.sequenceField().text).isEqualTo("4")
        Assertions.assertThat(runsTableItems).hasSize(3)
        Assertions.assertThat(runsTableItems)
                .extracting("sequence", "registrationNumbers")
                .containsExactly(
                        Tuple(3, "9 SM"),
                        Tuple(2, "33 SM"),
                        Tuple(1, "1 HS")
                )

        addNextDriverPage.writeInNumbersField("40 GS")
        addNextDriverPage.doAddSelectedRegistration()

        Assertions.assertThat(addNextDriverPage.sequenceField().text).isEqualTo("5")
        Assertions.assertThat(runsTableItems)
                .extracting("sequence", "registrationNumbers")
                .containsExactly(
                        Tuple(4, "40 GS"),
                        Tuple(3, "9 SM"),
                        Tuple(2, "33 SM"),
                        Tuple(1, "1 HS")
                )
    }

    @Test
    fun itShouldHaveTabFieldOrder(robot: FxRobot) {
        addNextDriverPage.focusNumbersField()
        org.testfx.assertions.api.Assertions.assertThat(addNextDriverPage.numbersField()).isFocused

        robot.type(KeyCode.TAB)
        org.testfx.assertions.api.Assertions.assertThat(tablePage.runsTable()).isFocused

        robot.type(KeyCode.TAB)
        org.testfx.assertions.api.Assertions.assertThat(addNextDriverPage.numbersField()).isFocused

        addNextDriverPage.focusRegistrationsListView()
        org.testfx.assertions.api.Assertions.assertThat(addNextDriverPage.registrationsListView()).isFocused

        robot.type(KeyCode.TAB)
        org.testfx.assertions.api.Assertions.assertThat(tablePage.runsTable()).isFocused
    }

    @Test
    fun `When the model runsSortedBySequence gets an item added, the table view should scroll to it`(robot: FxRobot) {
        // prepare the table with enough items to guarantee the table has to scroll
        val fastAddNextDriverPage = FastAddNextDriverPage(robot)
        val registration0 = addNextDriverPage.registrationsListView().items[0]
        for (i in 0..24) {
            fastAddNextDriverPage.selectRegistration(registration0)
            fastAddNextDriverPage.doAddSelectedRegistration()
        }

        // TODO: convert to awaitility
        val latch = CountDownLatch(1)
        tablePage.runsTable().setOnScrollTo {
            try {
                Assertions.assertThat(it.scrollTarget).isEqualTo(0)
            } finally {
                latch.countDown()
            }
        }

        val registration1 = addNextDriverPage.registrationsListView().items[1]
        addNextDriverPage.selectRegistration(registration1)
        addNextDriverPage.doAddSelectedRegistration()

        latch.await()
    }

    @Test
    fun `When clicking the table, it should select clicked run`(robot: FxRobot) {
        val table = tablePage.runsTable()
        val fastAddNextDriverPage = FastAddNextDriverPage(robot)
        for (i in 0..4) {
            val registration = fastAddNextDriverPage.registrationsListView().items[i]
            fastAddNextDriverPage.selectRegistration(registration)
            fastAddNextDriverPage.doAddSelectedRegistration()
        }

        tablePage.selectSequence(1)
        Assertions.assertThat(table.selectionModel.selectedIndex).isEqualTo(4)

        tablePage.selectSequence(5)
        Assertions.assertThat(table.selectionModel.selectedIndex).isEqualTo(0)

        tablePage.selectSequence(3)
        Assertions.assertThat(table.selectionModel.selectedIndex).isEqualTo(2)
    }

    @Test
    fun `When tabbing from numbers field, it should select first run without time`(robot: FxRobot) {
        val inputFile = startFileInputTimer()

        addNextDriverPage.writeInNumbersField("1 HS")
        addNextDriverPage.doAddSelectedRegistration()
        addNextDriverPage.writeInNumbersField("33 SM")
        addNextDriverPage.doAddSelectedRegistration()
        robot.type(KeyCode.TAB)

        org.testfx.assertions.api.Assertions.assertThat(tablePage.runsTable()).isFocused
        Assertions.assertThat(tablePage.runsTable().selectionModel.selectedIndex).isEqualTo(1)

        receiveTime(inputFile," 123450")

        addNextDriverPage.focusNumbersField()
        robot.type(KeyCode.TAB)

        org.testfx.assertions.api.Assertions.assertThat(tablePage.runsTable()).isFocused
        Assertions.assertThat(tablePage.runsTable().selectionModel.selectedIndex).isEqualTo(0)

        addNextDriverPage.focusNumbersField()
        receiveTime(inputFile, " 234560")
        robot.type(KeyCode.TAB)

        org.testfx.assertions.api.Assertions.assertThat(tablePage.runsTable()).isFocused
        Assertions.assertThat(tablePage.runsTable().selectionModel.selectedIndex).isEqualTo(0)

        addNextDriverPage.writeInNumbersField("13 SSC")
        addNextDriverPage.doAddSelectedRegistration()
        addNextDriverPage.writeInNumbersField("3 SSC")
        addNextDriverPage.doAddSelectedRegistration()
        robot.type(KeyCode.TAB)

        org.testfx.assertions.api.Assertions.assertThat(tablePage.runsTable()).isFocused
        Assertions.assertThat(tablePage.runsTable().selectionModel.selectedIndex).isEqualTo(1)

        stopTimer()
    }


    @Test
    fun `When user clears time it should prompt before clearing`(robot: FxRobot) {
        val inputFile = startFileInputTimer()
        addNextDriverPage.writeInNumbersField("1 HS")
        addNextDriverPage.doAddSelectedRegistration()
        receiveTime(inputFile, " 123450")
        Assumptions.assumeThat(tablePage.runsTable().items[0].rawTime).isNotNull()

        tablePage.keyboardShortcutClearTime(1)
        robot.clickOn("OK")

        Assertions.assertThat(tablePage.runsTable().items[0])
                .hasFieldOrPropertyWithValue("rawTime", null)
    }

    @Test // https://github.com/caeos/coner-drs/issues/48
    fun whenItHasReceivedTimesCreatingNewRunsNextAddedDriverShouldntGetDuplicateSequenceNumber() {
        val inputFile = startFileInputTimer()
        receiveTime(inputFile, " 987650")
        receiveTime(inputFile, " 876540")
        addNextDriverPage.writeInNumbersField("1 HS")

        addNextDriverPage.doAddSelectedRegistration()

        await().untilAsserted {
            assertThat(tablePage.runsTable().items).all {
                hasSize(2)
                index(0).all {
                    prop(Run::sequence).isEqualTo(2)
                    prop(Run::rawTime).isEqualTo(BigDecimal.valueOf(45678, 3))
                    prop(Run::registration).isNull()
                }
                index(1).all {
                    prop(Run::sequence).isEqualTo(1)
                    prop(Run::rawTime).isEqualTo(BigDecimal.valueOf(56789, 3))
                    prop(Run::registration).isNotNull()
                }
            }
        }
    }

    private fun startFileInputTimer(): Path {
        rightDrawerPage.expandTimer()
        timerPage.pressConfigure()
        val timerConfigurationPage = timerPage.toTimerConfigurationPage()
        timerConfigurationPage.chooseType(TimerConfiguration.FileInput::class)
        val inputFile = createTempFile(directory = tempDir.toFile()).toPath()
        timerConfigurationPage.chooseFile(inputFile)
        timerConfigurationPage.pressApply()
        timerPage.pressStart()
        rightDrawerPage.collapseTimer()
        return inputFile
    }

    private fun stopTimer() {
        rightDrawerPage.expandTimer()
        rightDrawerPage.toTimerPage().pressStop()
        rightDrawerPage.collapseTimer()
    }

    private fun receiveTime(inputFile: Path, time: String) {
        val timeAsBigDecimal = BigDecimal.valueOf(time.reversed().trim().toLong(), 3)
        val beforeCount = tablePage.runsTable().items.count { it.rawTime == timeAsBigDecimal }
        Files.write(inputFile, listOf(time), StandardOpenOption.APPEND)
        val afterCount = beforeCount + 1
        await().until {
            tablePage.runsTable().items.count { it.rawTime == timeAsBigDecimal } == afterCount
        }
    }

}
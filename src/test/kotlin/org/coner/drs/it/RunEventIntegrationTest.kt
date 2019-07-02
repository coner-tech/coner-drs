package org.coner.drs.it

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import javafx.scene.input.KeyCode
import me.carltonwhitehead.tornadofx.junit5.*
import me.carltonwhitehead.tornadofx.junit5.App
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assumptions
import org.assertj.core.groups.Tuple
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Run
import org.coner.drs.io.gateway.EventGateway
import org.coner.drs.it.util.FilesystemFixture
import org.coner.drs.it.util.IntegrationTestApp
import org.coner.drs.test.fixture.integration.crispyfish.classdefinition.Thscc2019V0Classes
import org.coner.drs.test.fixture.integration.crispyfish.event.Thscc2019Points1
import org.coner.drs.test.page.AddNextDriverPage
import org.coner.drs.test.page.RunEventPage
import org.coner.drs.test.page.RunEventTablePage
import org.coner.drs.test.page.fast.FastChooseEventPage
import org.coner.drs.test.page.fast.FastStartPage
import org.coner.drs.test.page.real.RealAlterDriverSequencePage
import org.coner.drs.test.page.real.RealRunEventPage
import org.coner.drs.test.page.real.RealRunEventTablePage
import org.coner.drs.ui.chooseevent.ChooseEventModel
import org.coner.drs.ui.chooseevent.ChooseEventTableView
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import org.testfx.api.FxRobot
import tornadofx.*
import java.nio.file.Path
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
        val latch = CountDownLatch(2)
        val fastChooseEventPage = FastChooseEventPage(robot)
        find<ChooseEventModel>(app.scope).dockedProperty.onChangeOnce { runLater { latch.countDown() } }
        find<ChooseEventTableView>(app.scope).root.items.onChange { runLater { latch.countDown() } }
        FX.runAndWait { find<EventGateway>(app.scope).save(event) }
        latch.await(1, TimeUnit.SECONDS)
        fastChooseEventPage.selectEvent { it.id == event.id }
        fastChooseEventPage.clickStartButton()
        this.event = find<EventGateway>(app.scope).list().first()
        this.page = RealRunEventPage(robot)
        this.addNextDriverPage = page.toAddNextDriverPage()
        this.tablePage = RealRunEventTablePage(robot)
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
                        Tuple(1, "3 SSC"),
                        Tuple(2, "1 HS")
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
                        Tuple(1, "1 HS"),
                        Tuple(2, "33 SM"),
                        Tuple(3, "9 SM")
                )

        addNextDriverPage.writeInNumbersField("40 GS")
        addNextDriverPage.doAddSelectedRegistration()

        Assertions.assertThat(addNextDriverPage.sequenceField().text).isEqualTo("5")
        Assertions.assertThat(runsTableItems)
                .extracting("sequence", "registrationNumbers")
                .containsExactly(
                        Tuple(1, "1 HS"),
                        Tuple(2, "33 SM"),
                        Tuple(3, "9 SM"),
                        Tuple(4, "40 GS")
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
    }
}
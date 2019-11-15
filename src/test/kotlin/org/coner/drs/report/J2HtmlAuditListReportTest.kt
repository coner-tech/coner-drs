package org.coner.drs.report

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEqualTo
import me.carltonwhitehead.tornadofx.junit5.SetupApp
import me.carltonwhitehead.tornadofx.junit5.TornadoFxAppExtension
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.io.DrsIoController
import org.coner.drs.io.gateway.EventGateway
import org.coner.drs.io.gateway.RegistrationGateway
import org.coner.drs.io.gateway.RunGateway
import org.coner.drs.test.fixture.TestEventFixture
import org.coner.drs.util.NumberFormat
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import tornadofx.*
import java.awt.Desktop
import java.io.File
import java.util.*

@ExtendWith(TornadoFxAppExtension::class)
class J2HtmlAuditListReportTest {

    lateinit var scope: Scope

    @SetupApp
    fun setupApp() = App().apply {
        this@J2HtmlAuditListReportTest.scope = scope
    }

    @BeforeEach
    fun before() {

    }

    @Test
    fun itShouldRender(@TempDir tempDir: File) {
        val fixture = TestEventFixture.Thscc2019Points9.factory(tempDir)
        val eventId = fixture.source.eventIds.single()
        val event = loadEvent(fixture, eventId)

        val actual = J2HtmlAuditListReport(event, NumberFormat.forRunTimes()).render()

//        Desktop.getDesktop()
//                .open(
//                        tempDir.resolve("itShouldRender.html").apply {
//                            writeText(actual)
//                        }
//                )
//
//        Thread.sleep(1000)

        assertThat(Jsoup.parse(actual)).all {
            hasTitleElementText("THSCC Points Autocross #9 @ Danville Regional Airport - 2019-10-27 - Audit List")
            onRunTableRowNumber(1, "first clean run") {
                sequenceIsEqualTo("1")
                numbersIsEqualTo("58 FS")
                penaltyIsBlank()
            }
            onRunTableRowNumber(2, "first coned run") {
                sequenceIsEqualTo("2")
                numbersIsEqualTo("15 SSC")
                penaltyIsOnly("+2")
            }
            onRunTableRowNumber(3, "first dnf run") {
                sequenceIsEqualTo("3")
                numbersIsEqualTo("52 GS")
                penaltyIsOnly("DNF")
            }
            onRunTableRowNumber(94, "first rerun") {
                sequenceIsEqualTo("94")
                numbersIsEqualTo("40 GS")
                penaltyIsOnly("RRN")
            }
            onRunTableRowNumber(60, "first run with multiple penalty types") {
                sequenceIsEqualTo("60")
                numbersIsEqualTo("58 NOV GS")
                hasPenalty(1, "DNF")
                hasPenalty(2, "+1")
            }
        }
    }

}

private fun J2HtmlAuditListReportTest.loadEvent(fixture: TestEventFixture.Instance, eventId: UUID): RunEvent {
    val drsIo: DrsIoController = find(this.scope)
    drsIo.open(
            pathToDrsDatabase = fixture.digitalRawSheetDatabase,
            pathToCrispyFishDatabase = fixture.crispyFishDatabase
    )
    val eventGateway: EventGateway = find(scope)
    val event = eventGateway.list().single { it.id == eventId }.let {
        eventGateway.asRunEvent(it)!!
    }
    val runGateway: RunGateway = find(scope)
    event.runs.addAll(runGateway.list(event).blockingGet())
    val registrationGateway: RegistrationGateway = find(scope)
    runGateway.hydrateWithRegistrationMetadata(event.runs, registrationGateway.list(event).blockingGet())
    return event
}

private fun Assert<Document>.hasTitleElementText(titleText: String) {
    transform("title element") { it.select("html head title").text() }.isEqualTo(titleText)
}

private fun Assert<Document>.onRunTableRowNumber(n: Int, name: String? = null, body: Assert<Elements>.() -> Unit) {
    transform(name ?: this.name) { it.select("table tbody tr:nth-child($n)") }.all(body)
}

private fun Assert<Elements>.sequenceIsEqualTo(expected: String) {
    transform("sequence of $name") { it.select("td.sequence").text() }.isEqualTo(expected)
}

private fun Assert<Elements>.numbersIsEqualTo(expected: String) {
    transform("numbers of $name") { it.select("td.numbers").text() }.isEqualTo(expected)
}

private fun Assert<Elements>.penaltyIsBlank() {
    transform("penalty of $name") { it.select("td.penalty").hasText() }.isFalse()
}

private fun Assert<Elements>.penaltyIsOnly(expected: String) {
    transform("penalty of $name") { it.select("td.penalty span:only-child").text() }.isEqualTo(expected)
}

private fun Assert<Elements>.hasPenalty(n: Int, expected: String) {
    transform("nth-penalty($n) of $name") { it.select("td.penalty span:nth-child($n)").text() }.isEqualTo(expected)
}
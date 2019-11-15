package org.coner.drs.report

import me.carltonwhitehead.tornadofx.junit5.SetupApp
import me.carltonwhitehead.tornadofx.junit5.TornadoFxAppExtension
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.io.DrsIoController
import org.coner.drs.io.gateway.EventGateway
import org.coner.drs.io.gateway.RegistrationGateway
import org.coner.drs.io.gateway.RunGateway
import org.coner.drs.test.fixture.TestEventFixture
import org.coner.drs.util.NumberFormat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import tornadofx.*
import java.awt.Desktop
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

@ExtendWith(TornadoFxAppExtension::class)
class AuditListReportTest {

    lateinit var scope: Scope

    @SetupApp
    fun setupApp() = App().apply {
        this@AuditListReportTest.scope = scope
    }

    @BeforeEach
    fun before() {

    }

    @Test
    fun itShouldRender(@TempDir tempDir: File) {
        val fixture = TestEventFixture.Thscc2019Points9.factory(tempDir)
        val eventId = fixture.source.eventIds.single()
        val event = loadEvent(fixture, eventId)

        val report = AuditListReport(event, NumberFormat.forRunTimes())
        val render = report.render()

        val outputFile = tempDir.resolve("report.html")
        outputFile.writeText(render)
//        Desktop.getDesktop().open(outputFile)
//        Thread.sleep(TimeUnit.SECONDS.toMillis(1))
    }

}

private fun AuditListReportTest.loadEvent(fixture: TestEventFixture.Instance, eventId: UUID): RunEvent {
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
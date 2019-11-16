package org.coner.drs.test.fixture

import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.io.DrsIoController
import org.coner.drs.io.gateway.EventGateway
import org.coner.drs.io.gateway.RegistrationGateway
import org.coner.drs.io.gateway.RunGateway
import org.coner.drs.report.J2HtmlAuditListReportTest
import tornadofx.*
import java.util.*

object FixtureUtil {

    fun loadRunEvent(app: App, fixture: TestEventFixture.Instance, eventId: UUID): RunEvent {
        val drsIo: DrsIoController = find(app.scope)
        drsIo.open(
                pathToDrsDatabase = fixture.digitalRawSheetDatabase,
                pathToCrispyFishDatabase = fixture.crispyFishDatabase
        )
        val eventGateway: EventGateway = find(app.scope)
        val event = eventGateway.list().single { it.id == eventId }.let {
            eventGateway.asRunEvent(it)!!
        }
        val runGateway: RunGateway = find(app.scope)
        event.runs.addAll(runGateway.list(event).blockingGet())
        val registrationGateway: RegistrationGateway = find(app.scope)
        runGateway.hydrateWithRegistrationMetadata(event.runs, registrationGateway.list(event).blockingGet())
        return event
    }
}
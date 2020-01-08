package org.coner.drs.report

import me.carltonwhitehead.tornadofx.junit5.SetupApp
import me.carltonwhitehead.tornadofx.junit5.TornadoFxAppExtension
import org.coner.drs.test.fixture.FixtureUtil
import org.coner.drs.test.fixture.TestEventFixture
import org.coner.drs.util.NumberFormat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.io.TempDir
import tornadofx.*
import java.awt.Desktop
import java.io.File

@ExtendWith(TornadoFxAppExtension::class)
class KotlinxHtmlAuditListReportTest {
    lateinit var app: App

    @SetupApp
    fun setupApp() = App().apply {
        app = this
    }

    @Test
    fun itShouldRender(@TempDir tempDir: File) {
        val fixture = TestEventFixture.Thscc2019Points9.factory(tempDir)
        val eventId = fixture.source.eventIds.single()
        val event = FixtureUtil.loadRunEvent(app, fixture, eventId)

        val actual = KotlinxHtmlAuditListReport(NumberFormat.forRunTimes()).render(event)

        AuditListReportTestUtil.assert(actual)
    }
}
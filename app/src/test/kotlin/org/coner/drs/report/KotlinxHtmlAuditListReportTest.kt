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
package org.coner.drs.it

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.assertj.core.api.Assumptions
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Run
import org.coner.drs.io.gateway.EventGateway
import org.coner.drs.test.fixture.integration.crispyfish.classdefinition.Thscc2019V0Classes
import org.coner.drs.test.fixture.integration.crispyfish.event.Thscc2019Points1
import org.coner.drs.test.page.fast.FastChooseEventPage
import org.coner.drs.test.page.real.RealRunEventPage
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tornadofx.*
import java.time.LocalDate

class RunEventIntegrationTest : BaseIntegrationTest() {

    private lateinit var event: Event
    private lateinit var page: RealRunEventPage

    @BeforeEach
    fun beforeEach() {
        val classDefinitionFile = Thscc2019V0Classes.produceClassDefinitionFile(crispyFishDatabase!!)
        val eventControlFile = Thscc2019Points1.produceEventControlFile(crispyFishDatabase!!, classDefinitionFile)
        val event = Event(
                date = LocalDate.parse("2019-03-03"),
                name = "RunEventIntegrationTest",
                crispyFishMetadata = Event.CrispyFishMetadata(
                        classDefinitionFile = classDefinitionFile.file,
                        eventControlFile = eventControlFile.file
                )
        )
        val app = this.app!!
        val robot = this.robot!!
        robot.interact { find<EventGateway>(app.scope).save(event) }
        val fastChooseEventPage = FastChooseEventPage(robot)
        robot.interact { fastChooseEventPage.selectEvent { it.id == event.id } }
        robot.interact { fastChooseEventPage.clickStartButton() }

        this.event = find<EventGateway>(app.scope).list().first()
        this.page = RealRunEventPage(robot)
    }

    @Test
    fun itShouldDisplayEvent() {
        assertThat(page.root().text).isEqualTo(event.name)
    }

    @Test
    fun itShouldAddNextDriver() {
        Assumptions.assumeThat(page.runsTable().items).hasSize(0)

        page.clickOnAddNextDriverNumbersField()
        page.fillAddNextDriverNumbersField("1 HS")
        page.clickOnAddNextDriverAddButton()

        assertThat(page.runsTable()).all {
            this.prop("items") { it.items }.all {
                hasSize(1)
                index(0).all {
                    prop(Run::registrationNumbers).isEqualTo("1 HS")
                }
            }
        }
    }
}
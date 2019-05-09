package org.coner.drs.test.page.real

import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import org.coner.drs.domain.entity.Event
import org.coner.drs.test.page.ChooseEventPage
import org.testfx.api.FxRobot

open class RealChooseEventPage(protected val robot: FxRobot) : ChooseEventPage {

    override fun root() = robot.lookup("#choose-event")
            .query() as BorderPane

    override fun startButton() = robot.from(root())
            .lookup("#start")
            .queryButton()

    override fun eventsTable() = robot.from(root())
            .lookup("#events")
            .query() as TableView<Event>

    override fun selectEvent(matcher: (Event) -> Boolean) {
        throw UnsupportedOperationException()
    }

    override fun clickStartButton() {
        robot.clickOn(startButton())
    }
}
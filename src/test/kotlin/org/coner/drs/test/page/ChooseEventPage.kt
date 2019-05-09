package org.coner.drs.test.page

import javafx.scene.control.Button
import javafx.scene.control.TableView
import javafx.scene.layout.BorderPane
import org.coner.drs.domain.entity.Event

interface ChooseEventPage {

    fun root(): BorderPane

    fun eventsTable(): TableView<Event>

    fun selectEvent(matcher: (Event) -> Boolean)

    fun startButton(): Button

    fun clickStartButton()
}
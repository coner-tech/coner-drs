package org.coner.drs.test.page

import javafx.scene.control.*
import org.coner.drs.domain.entity.Run
import tornadofx.*

interface RunEventPage {

    fun root(): TitledPane
    fun onTopPage(fn: RunEventTopPage.() -> Unit)
    fun toAddNextDriverPage(): AddNextDriverPage
    fun toTablePage(): RunEventTablePage
    fun toRightDrawerPage(): RunEventRightDrawerPage
}
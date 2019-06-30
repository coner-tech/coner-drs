package org.coner.drs.test.page

import javafx.scene.control.*
import org.coner.drs.domain.entity.Run
import tornadofx.*

interface RunEventPage {

    fun root(): TitledPane
    fun toAddNextDriverPage(): AddNextDriverPage
    fun toTablePage(): RunEventTablePage
}
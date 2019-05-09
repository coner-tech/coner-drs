package org.coner.drs.test.page

import javafx.scene.control.*
import org.coner.drs.domain.entity.Run
import tornadofx.*

interface RunEventPage {

    fun root(): TitledPane

    fun addNextDriverForm(): Form

    fun addNextDriverNumbersField(): TextField

    fun clickOnAddNextDriverNumbersField()

    fun fillAddNextDriverNumbersField(numbers: String)

    fun addNextDriverAddButton(): SplitMenuButton

    fun clickOnAddNextDriverAddButton()

    fun runsTable(): TableView<Run>
}
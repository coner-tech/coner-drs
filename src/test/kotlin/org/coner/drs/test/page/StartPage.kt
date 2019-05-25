package org.coner.drs.test.page

import javafx.scene.control.Button
import javafx.scene.control.TextInputControl
import javafx.scene.layout.StackPane
import java.io.File
import java.nio.file.Path

interface StartPage {

    fun root(): StackPane

    fun rawSheetDatabaseField(): TextInputControl

    fun setRawSheetDatabase(file: Path)

    fun crispyFishDatabaseField(): TextInputControl

    fun setCrispyFishDatabase(file: File)

    fun startButton(): Button

    fun clickStartButton()
}
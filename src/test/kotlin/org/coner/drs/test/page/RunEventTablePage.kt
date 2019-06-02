package org.coner.drs.test.page

import javafx.scene.control.TableCell
import javafx.scene.control.TableView
import org.coner.drs.domain.entity.Run
import tornadofx.*

interface RunEventTablePage {
    fun root(): Form
    fun runsTable(): TableView<Run>
    fun tableCellForSequence(sequence: Int): TableCell<Run, Int>
    fun selectSequence(sequence: Int)
    fun clickInsertDriverIntoSequence(sequence: Int)
    fun keyboardShortcutInsertDriverIntoSequence(sequence: Int)
}
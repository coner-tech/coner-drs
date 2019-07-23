package org.coner.drs.test.page

import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.layout.BorderPane

interface AlterDriverSequencePage {
    fun root(): BorderPane
    fun bottomButtonBar(): ButtonBar
    fun okButton(): Button
    fun cancelButton(): Button
    fun clickOkButton()
    fun clickCancelButton()
    fun toSpecifyDriverSequenceAlterationPage(): SpecifyDriverSequenceAlterationPage
    fun toPreviewAlteredDriverSequencePage(): PreviewAlteredDriverSequencePage
}
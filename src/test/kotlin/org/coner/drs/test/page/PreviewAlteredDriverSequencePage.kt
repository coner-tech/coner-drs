package org.coner.drs.test.page

import javafx.scene.control.TableView
import org.coner.drs.ui.alterdriversequence.PreviewAlteredDriverSequenceResult
import tornadofx.*

interface PreviewAlteredDriverSequencePage {

    fun root(): Form
    fun runsTable(): TableView<PreviewAlteredDriverSequenceResult.Run>
}
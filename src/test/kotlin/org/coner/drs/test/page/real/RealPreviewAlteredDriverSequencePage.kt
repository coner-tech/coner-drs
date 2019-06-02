package org.coner.drs.test.page.real

import javafx.scene.control.TableView
import org.coner.drs.test.page.PreviewAlteredDriverSequencePage
import org.coner.drs.ui.alterdriversequence.PreviewAlteredDriverSequenceResult
import org.testfx.api.FxRobot
import tornadofx.*

open class RealPreviewAlteredDriverSequencePage(private val robot: FxRobot) : PreviewAlteredDriverSequencePage {

    override fun root() = robot.lookup("#preview-altered-driver-sequence-view")
            .query<Form>()

    override fun runsTable() = robot.from(root())
            .lookup("#runs-table")
            .query<TableView<PreviewAlteredDriverSequenceResult.Run>>()
}
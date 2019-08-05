package org.coner.drs.ui.alterdriversequence

import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class PreviewAlteredDriverSequenceModel : ViewModel() {

    val previewResultProperty = SimpleObjectProperty<PreviewAlteredDriverSequenceResult>(this, "previewResult")
    var previewResult by previewResultProperty

    val previewResultRunsProperty = SimpleListProperty<PreviewAlteredDriverSequenceResult.Run>(this, "previewResultRuns")
    var previewResultRuns by previewResultRunsProperty

}

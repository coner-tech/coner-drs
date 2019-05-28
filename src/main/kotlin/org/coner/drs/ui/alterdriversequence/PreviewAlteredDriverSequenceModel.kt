package org.coner.drs.ui.alterdriversequence

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class PreviewAlteredDriverSequenceModel : ViewModel() {

    val previewResultProperty = SimpleObjectProperty<PreviewAlteredDriverSequenceResult>(this, "runs")
    var previewResult by previewResultProperty

}

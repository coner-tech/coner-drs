package org.coner.drs.ui.alterdriversequence

import org.coner.drs.domain.mapper.RunMapper
import tornadofx.*

class PreviewAlteredDriverSequenceController : Controller() {

    private val model: PreviewAlteredDriverSequenceModel by inject()
    private val alterDriverSequenceModel: AlterDriverSequenceModel by inject()

    init {
        alterDriverSequenceModel.previewResultProperty.onChange { result ->
            model.previewResult = when {
                result != null -> RunMapper.toPreviewAlteredDriverSequenceResult(result)
                else -> PreviewAlteredDriverSequenceResult(observableListOf(), null)
            }
        }
    }

}
package org.coner.drs.ui.alterdriversequence

import org.coner.drs.domain.mapper.RunMapper
import tornadofx.*

class PreviewAlteredDriverSequenceController : Controller() {

    private val alterDriverSequenceModel: AlterDriverSequenceModel by inject()
    private val model: PreviewAlteredDriverSequenceModel by inject()

    init {
        alterDriverSequenceModel.previewResultProperty.onChange { result ->
            model.previewResult = when {
                result != null -> RunMapper.toPreviewAlteredDriverSequenceResult(result)
                else -> PreviewAlteredDriverSequenceResult(observableListOf())
            }
        }
    }
}
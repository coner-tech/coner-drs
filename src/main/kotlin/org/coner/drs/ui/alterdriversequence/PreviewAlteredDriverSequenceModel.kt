package org.coner.drs.ui.alterdriversequence

import com.github.thomasnield.rxkotlinfx.toBinding
import com.github.thomasnield.rxkotlinfx.toObservable
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import org.coner.drs.domain.entity.Run
import tornadofx.*

class PreviewAlteredDriverSequenceModel : ViewModel() {

    private val model: AlterDriverSequenceModel by inject()

    private val runsBinding = model.previewResultProperty.toObservable()
            .map { it.runs.toObservable() }
            .toBinding()
    val runsProperty = SimpleObjectProperty<ObservableList<Run>>(this, "runs").apply {
        bind(runsBinding)
    }
    var runs by runsProperty

}

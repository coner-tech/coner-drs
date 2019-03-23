package org.coner.drs.ui.runevent

import io.reactivex.disposables.CompositeDisposable
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.entity.TimerConfiguration
import tornadofx.*

class RunEventModel : ViewModel() {
    val eventProperty = SimpleObjectProperty<RunEvent>()
    var event by eventProperty

    val disposables = CompositeDisposable()

    val controlTextProperty = SimpleStringProperty(this, "controlText")
    var controlText by controlTextProperty

    val timerConfigurationProperty = SimpleObjectProperty<TimerConfiguration>(this, "timerConfiguration", null)
    var timerConfiguration by timerConfigurationProperty

    val timerConfigurationTextProperty = SimpleStringProperty(this, "timerConfigurationText", null)
    var timerConfigurationText by timerConfigurationTextProperty

}
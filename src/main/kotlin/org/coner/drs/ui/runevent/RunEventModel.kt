package org.coner.drs.ui.runevent

import io.reactivex.disposables.CompositeDisposable
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import org.coner.drs.Event
import org.coner.drs.Registration
import org.coner.drs.Run
import org.coner.drs.TimerConfiguration
import tornadofx.*

class RunEventModel : ViewModel() {
    val runs = observableList<Run>()
    val registrations = observableList<Registration>()
    val eventProperty = SimpleObjectProperty<Event>()
    var event by eventProperty

    val disposables = CompositeDisposable()

    val controlTextProperty = SimpleStringProperty(this, "controlText")
    var controlText by controlTextProperty

    val timerConfigurationProperty = SimpleObjectProperty<TimerConfiguration>(this, "timerConfiguration", null)
    var timerConfiguration by timerConfigurationProperty

    val timerConfigurationTextProperty = SimpleStringProperty(this, "timerConfigurationText", null)
    var timerConfigurationText by timerConfigurationTextProperty

}
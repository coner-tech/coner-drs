package org.coner.drs.ui.chooseevent

import io.reactivex.disposables.CompositeDisposable
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import org.coner.drs.domain.entity.Event
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class ChooseEventModel : ViewModel() {
    val events = observableListOf<Event>()
    val choiceProperty = SimpleObjectProperty<Event>(this, "event")
    var choice by choiceProperty

    val disposables = CompositeDisposable()

    val dockedProperty = SimpleBooleanProperty(this, "docked", false)
    var docked by dockedProperty

}
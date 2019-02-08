package org.coner.drs.ui.chooseevent

import io.reactivex.disposables.CompositeDisposable
import javafx.beans.property.SimpleObjectProperty
import org.coner.drs.domain.entity.Event
import tornadofx.*

class ChooseEventModel : ViewModel() {
    val events = observableList<Event>()
    val choiceProperty = SimpleObjectProperty<Event>(this, "event")
    var choice by choiceProperty

    val disposables = CompositeDisposable()
}
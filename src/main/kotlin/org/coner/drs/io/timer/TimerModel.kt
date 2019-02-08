package org.coner.drs.io.timer

import javafx.beans.property.SimpleObjectProperty
import org.coner.timer.Timer
import tornadofx.*

class TimerModel : ViewModel() {
    val timerProperty = SimpleObjectProperty<Timer<*, *>>(this, "timer", null)
    var timer by timerProperty
}
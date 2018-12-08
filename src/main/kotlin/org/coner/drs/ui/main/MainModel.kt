package org.coner.drs.ui.main

import javafx.beans.property.SimpleObjectProperty
import org.coner.drs.ui.main.Screen
import tornadofx.*

class MainModel : ViewModel() {
    val screenProperty = SimpleObjectProperty<Screen>(this, "screen")
    var screen by screenProperty
}
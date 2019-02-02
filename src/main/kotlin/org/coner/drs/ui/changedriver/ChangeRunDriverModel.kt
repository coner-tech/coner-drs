package org.coner.drs.ui.changedriver

import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class ChangeRunDriverModel : ViewModel() {

    val numbersProperty = SimpleStringProperty(this, "numbers", "")
    var numbers by numbersProperty

}
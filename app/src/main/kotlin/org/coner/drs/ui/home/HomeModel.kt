package org.coner.drs.ui.home

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class HomeModel : ViewModel() {

    val katanaScopeProperty = SimpleObjectProperty<HomeScope>(this, "katanaScope")
    var katanaScope by katanaScopeProperty
}
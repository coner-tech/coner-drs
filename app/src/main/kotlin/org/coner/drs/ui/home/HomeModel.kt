package org.coner.drs.ui.home

import javafx.beans.property.SimpleObjectProperty
import tornadofx.*

class HomeModel : ViewModel() {

    val katanaScopeProperty = SimpleObjectProperty<HomeKatanaScope>(this, "katanaScope")
    var katanaScope by katanaScopeProperty
}
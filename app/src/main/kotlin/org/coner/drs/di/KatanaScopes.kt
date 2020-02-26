package org.coner.drs.di

import javafx.beans.property.SimpleObjectProperty
import org.coner.drs.ui.home.HomeKatanaScope
import org.coner.drs.ui.runevent.RunEventKatanaScope
import tornadofx.getValue
import tornadofx.setValue

class KatanaScopes {
    val homeProperty = SimpleObjectProperty<HomeKatanaScope>(this, "home")
    var home by homeProperty

    val runEventProperty = SimpleObjectProperty<RunEventKatanaScope>(this, "runEvent")
    var runEvent by runEventProperty

}
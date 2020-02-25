package org.coner.drs.di

import javafx.beans.property.SimpleObjectProperty
import org.coner.drs.ui.home.HomeKatanaScope
import tornadofx.*

object KatanaScopes {
    val homeScopeProperty = SimpleObjectProperty<HomeKatanaScope>(this, "")
    var home by homeScopeProperty
}
package org.coner.drs.ui.runevent

import org.coner.drs.di.KatanaModules
import org.rewedigital.katana.Component
import tornadofx.*

class RunEventContainer : ViewModel() {

    val component by lazy { Component(KatanaModules.numberFormats) }

}
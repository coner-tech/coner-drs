package org.coner.drs.ui.run_event

import javafx.geometry.Side
import tornadofx.*

class RunEventLeftDrawerView : View() {
    override val root = drawer(side = Side.LEFT) {
        item<AddNextDriverView>(expanded = true)
    }
}
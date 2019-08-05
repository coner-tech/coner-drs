package org.coner.drs.ui.runevent

import javafx.geometry.Side
import tornadofx.*

class RunEventRightDrawerView : View() {
    override val root = drawer(side = Side.RIGHT) {
        id = "run-event-right-drawer"
        item<TimerView>(showHeader = true) {
            id = "timer-item"
        }
    }
}
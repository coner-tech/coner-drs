package org.coner.drs.ui

import tornadofx.*

class OnTabEvent(val origin: Origin) : FXEvent() {
    enum class Origin {
        RunEventAddNextDriverNumbers,
        RunEventRuns
    }
}
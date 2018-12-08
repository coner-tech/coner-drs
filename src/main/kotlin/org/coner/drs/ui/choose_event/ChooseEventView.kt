package org.coner.drs.ui.choose_event

import tornadofx.*

class ChooseEventView : View("Choose Event") {
    override val root = borderpane {
        center<ChooseEventTableView>()
        bottom<ChooseEventBottomView>()
    }
}
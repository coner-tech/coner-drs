package org.coner.drs.ui.chooseevent

import tornadofx.*

class ChooseEventView : View("Choose Event") {
    override val root = borderpane {
        center<ChooseEventTableView>()
        bottom<ChooseEventBottomView>()
    }
}
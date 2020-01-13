package org.coner.drs.ui.helpabout

import org.coner.drs.ui.DrsStylesheet
import tornadofx.*

class HelpAboutApp : App(
        primaryView = HelpAboutView::class,
        stylesheet = DrsStylesheet::class
)
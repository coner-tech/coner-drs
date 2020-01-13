package org.coner.drs.ui.start

import javafx.geometry.Pos
import javafx.scene.control.MenuItem
import org.coner.drs.ui.DrsStylesheet
import org.coner.drs.ui.logo.LogoView
import tornadofx.*

class StartTopView : View() {

    var fileExit: MenuItem by singleAssign()
    var helpAbout: MenuItem by singleAssign()

    override val root = hbox(
            spacing = 16,
            alignment = Pos.CENTER_LEFT
    ) {
        add<LogoView>()
        menubar {
            menu("File") {
                item("Exit") {
                    fileExit = this
                }
            }
            menu("Help") {
                item("About") {
                    helpAbout = this
                }
            }
        }
        addClass(DrsStylesheet.topBar)
    }
}
package org.coner.drs.ui.start

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.MenuItem
import javafx.scene.layout.*
import org.coner.drs.DigitalRawSheetApp
import org.coner.drs.DrsStylesheet
import org.coner.drs.ui.logo.LogoView
import org.coner.style.ConerLogoPalette
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
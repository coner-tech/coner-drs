package org.coner.drs.ui.runevent

import javafx.geometry.Pos
import javafx.scene.control.MenuItem
import javafx.scene.layout.Priority
import org.coner.drs.DrsStylesheet
import org.coner.drs.ui.logo.LogoView
import tornadofx.*

class RunEventTopView : View() {

    private val controller: RunEventTopController by inject()
    private val model: RunEventModel by inject()
    var fileExit: MenuItem by singleAssign()
    var reportsAuditList: MenuItem by singleAssign()
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
            menu("Reports") {
                item("Audit List") {
                    reportsAuditList = this
                }
            }
            menu("Help") {
                item("About") {
                    helpAbout = this
                }
            }
        }
        pane {
            hgrow = Priority.ALWAYS
        }
        label(model.eventProperty.select { it.nameProperty })
        addClass(DrsStylesheet.topBar)
    }

    override fun onDock() {
        super.onDock()
        controller.docked()
    }

    override fun onUndock() {
        super.onUndock()
        controller.undocked()
    }
}
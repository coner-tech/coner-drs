package org.coner.drs.ui.helpabout

import javafx.geometry.Pos
import javafx.scene.control.TabPane
import org.coner.drs.ui.DrsStylesheet
import org.coner.drs.ui.icon.IconView
import org.coner.style.ConerFxStylesheet
import tornadofx.*

class HelpAboutView : View("About") {

    override val root = vbox {
        hbox {
            addClass(DrsStylesheet.topBar)
            add<IconView>(IconView::size to IconView.Size.X128)
            vbox {
                alignment = Pos.BOTTOM_LEFT
                label("Coner") {
                    addClass(ConerFxStylesheet.h1)
                }
                label("Digital Raw Sheet") {
                    addClass(ConerFxStylesheet.h2)
                }
            }
        }
        tabpane {
            tab<DescriptionView>()
            tab<LicenseView>()
            tab<AuthorsView>()
            tab<AcknowledgementsView>()
            this.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        }
    }

    override fun onDock() {
        super.onDock()
        currentStage?.let {
            it.minWidth = 480.0
            it.minHeight = 320.0
        }
    }

}
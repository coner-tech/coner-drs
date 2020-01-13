package org.coner.drs.ui.helpabout

import org.coner.drs.ui.icon.IconView
import tornadofx.*

class HelpAboutView : View("About") {

    override val root = vbox {
        hbox {
            add<IconView>(IconView::size to IconView.Size.X128)
            vbox {
                label("Coner") {
                }
                label("Digital Raw Sheet") {
                }
            }
        }
        tabpane {
            tab<DescriptionView>()
            tab<LicenseView>()
            tab<AuthorsView>()
            tab<AcknowledgementsView>()
        }
    }
}
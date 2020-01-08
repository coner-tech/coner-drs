package org.coner.drs.ui.logo

import javafx.scene.layout.StackPane
import org.coner.drs.DrsStylesheet
import tornadofx.*

class LogoView : Fragment() {
    val size: Size by param(Size.X96)

    override val root = stackpane {
        addClass(DrsStylesheet.logo)
        imageview(url = "/coner-logo/coner-logo_$size.png") {
            setId(DrsStylesheet.logoImage)
        }
    }

    enum class Size {
        X96,
        X128,
        X1024;

        override fun toString(): String {
            return name.substring(1)
        }}
}

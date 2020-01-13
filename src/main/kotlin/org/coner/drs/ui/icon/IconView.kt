package org.coner.drs.ui.icon

import javafx.scene.layout.StackPane
import org.coner.drs.DrsStylesheet
import tornadofx.*

class IconView : Fragment() {
    val size: Size by param(Size.X128)

    override val root = stackpane {
        addClass(DrsStylesheet.icon)
        imageview(url = "/coner-icon/coner-icon_$size.png") {
            setId(DrsStylesheet.logoImage)
        }
    }

    enum class Size {
        X16,
        X32,
        X48,
        X64,
        X128,
        X256,
        X1024,
        X3840;

        override fun toString(): String {
            return name.substring(1)
        }}
}

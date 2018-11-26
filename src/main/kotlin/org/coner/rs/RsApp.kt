package org.coner.rs

import javafx.scene.text.FontSmoothingType
import org.coner.style.ConerFxStylesheet
import tornadofx.*
import tornadofx.Stylesheet

class RsApp : App(
        primaryView = MainView::class,
        stylesheet = org.coner.rs.Stylesheet::class
)

class Stylesheet : Stylesheet(ConerFxStylesheet::class) {
}
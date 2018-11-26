package org.coner.rs

import javafx.scene.text.FontSmoothingType
import tornadofx.*
import tornadofx.Stylesheet

class RsApp : App(
        primaryView = MainView::class,
        stylesheet = org.coner.rs.Stylesheet::class
)

class Stylesheet : Stylesheet() {
    val text by cssclass()

    init {
        text {
            fontSmoothingType = FontSmoothingType.GRAY
        }
    }
}
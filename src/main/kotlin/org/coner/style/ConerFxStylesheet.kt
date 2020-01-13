package org.coner.style

import javafx.scene.paint.Color
import javafx.scene.paint.Stop
import javafx.scene.text.FontSmoothingType
import javafx.scene.text.FontWeight
import tornadofx.*


class ConerFxStylesheet : Stylesheet() {

    companion object {
        val h1 by cssclass("h1")
        val h2 by cssclass("h2")
    }

    init {
        root {
            accentColor = ConerLogoPalette.ORANGE
            focusColor = ConerLogoPalette.ORANGE
        }
        text {
            fontSmoothingType = FontSmoothingType.GRAY
        }
        h1 {
            fontSize = 24.pt
            fontWeight = FontWeight.BOLD
            padding = box(0.px, 8.px)
        }
        h2 {
            fontSize = 18.pt
            fontWeight = FontWeight.BOLD
            padding = box(0.px, 8.px)
        }
        select(button.and(default)) {
            baseColor = ConerLogoPalette.Triad.SECONDARY_1
        }
    }
}
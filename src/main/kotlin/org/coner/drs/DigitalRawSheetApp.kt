package org.coner.drs

import org.coner.style.ConerFxStylesheet
import tornadofx.*
import tornadofx.Stylesheet

class DigitalRawSheetApp : App(
        primaryView = MainView::class,
        stylesheet = org.coner.drs.Stylesheet::class
)

class Stylesheet : Stylesheet(ConerFxStylesheet::class) {
}
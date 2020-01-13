package org.coner.drs.ui

import javafx.scene.paint.Color
import org.coner.style.ConerFxStylesheet
import org.coner.style.ConerLogoPalette
import tornadofx.*

class DrsStylesheet : Stylesheet(ConerFxStylesheet::class) {

    companion object {
        val logo by cssclass("logo")
        val icon by cssclass("icon")
        val logoImage by cssid("image")
        val penalties by cssclass("penalties")
        val topBar by cssclass("top-bar")
    }

    init {
        topBar {
            label {
                textFill = Color.WHITE
            }
            padding = box(16.px)
            backgroundColor = multi(ConerLogoPalette.DARK_GRAY)
            menuBar {
                backgroundColor = multi(Color.TRANSPARENT)
            }
            menu {
                label {
                    textFill = Color.WHITE
                }
            }
            menuItem {
                label {
                    textFill = Color.BLACK
                }
                and(focused) {
                    label {
                        textFill = Color.WHITE
                    }
                }
            }
        }
    }

}
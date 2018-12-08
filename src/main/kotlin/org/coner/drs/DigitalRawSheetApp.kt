package org.coner.drs

import javafx.scene.image.Image
import javafx.stage.Stage
import org.coner.drs.ui.main.MainView
import org.coner.style.ConerFxStylesheet
import tornadofx.*
import tornadofx.Stylesheet

class DigitalRawSheetApp : App(
        primaryView = MainView::class,
        stylesheet = org.coner.drs.Stylesheet::class
) {
    override fun start(stage: Stage) {
        super.start(stage)
        stage.icons.addAll(
                listOf(16, 32, 48, 64, 128, 256, 512, 1024)
                        .map { Image("/coner-icon/coner-icon_$it.png") }
        )
        val uiComponent = stage.uiComponent<UIComponent>()!!
        stage.titleProperty().bind(
                stringBinding(uiComponent.titleProperty) {
                    "Coner Digital Raw Sheet - ${uiComponent.title}"
                })
        stage.minWidth = 1024.0
        stage.minHeight = 768.0
    }

}

class Stylesheet : Stylesheet(ConerFxStylesheet::class) {
}
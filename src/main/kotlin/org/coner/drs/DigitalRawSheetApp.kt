package org.coner.drs

import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage
import net.harawata.appdirs.AppDirsFactory
import org.coner.drs.ui.main.MainView
import org.coner.style.ConerFxStylesheet
import tornadofx.*
import tornadofx.Stylesheet
import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

class DigitalRawSheetApp : App(
        primaryView = MainView::class,
        stylesheet = DrsStylesheet::class
) {

    init {
        importStylesheet("/style/coner-unsafe.css")
    }

    val drsProperties by lazy {
        PropertyResourceBundle(resources.url("/drs.properties").openStream())
    }

    override val configBasePath: Path = Paths.get(URI.create("file://" + AppDirsFactory.getInstance()
            .getUserConfigDir(
                    "digital-raw-sheet",
                    drsProperties.getString("coner-drs.version"),
                    "coner"
            )
    ))

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
        stage.minHeight = 720.0
    }

}

fun main(args: Array<String>) {
    Application.launch(DigitalRawSheetApp::class.java, *args)
}

class DrsStylesheet : Stylesheet(ConerFxStylesheet::class) {

    companion object {
        val penalties by cssclass("penalties")
    }

}
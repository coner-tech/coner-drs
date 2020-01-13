package org.coner.drs

import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage
import net.harawata.appdirs.AppDirsFactory
import org.coner.drs.di.numberFormatModule
import org.coner.drs.di.reportModule
import org.coner.drs.ui.DrsStylesheet
import org.coner.drs.ui.main.MainView
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import tornadofx.*
import java.io.File
import java.nio.file.Path

open class DigitalRawSheetApp : App(
        primaryView = MainView::class,
        stylesheet = DrsStylesheet::class
), KatanaTrait {

    init {
        importStylesheet("/style/coner-unsafe.css")
    }
    override val configBasePath: Path = AppDirsFactory.getInstance()
            .getUserConfigDir(
                    "digital-raw-sheet",
                    "0.0.0",
                    "coner"
            ).let { userConfigDir: String -> File(userConfigDir).toPath() }

    override val component = Component(
            numberFormatModule,
            reportModule
    )

    open val forceExitOnStop = true

    override fun start(stage: Stage) {
        super.start(stage)
        println("DigitalRawSheetApp.start()")
        stage.icons.addAll(
                listOf(16, 32, 48, 64, 128, 256, 512, 1024)
                        .map { Image("/coner-icon/coner-icon_$it.png") }
        )
        val uiComponent = stage.uiComponent<UIComponent>()!!
        stage.titleProperty().bind(
                stringBinding(uiComponent.titleProperty) {
                    "Coner Digital Raw Sheet - ${uiComponent.title}"
                })
        stage.width = 1024.0
        stage.height = 720.0
        stage.minWidth = 1024.0
        stage.minHeight = 720.0
    }

    override fun stop() {
        super.stop()
        if (forceExitOnStop) {
            Runtime.getRuntime().exit(0)
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(DigitalRawSheetApp::class.java, *args)
}


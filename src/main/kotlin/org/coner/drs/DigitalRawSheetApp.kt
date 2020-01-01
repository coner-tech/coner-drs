package org.coner.drs

import javafx.application.Application
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.stage.Stage
import net.harawata.appdirs.AppDirsFactory
import org.coner.drs.di.numberFormatModule
import org.coner.drs.di.reportModule
import org.coner.drs.ui.main.MainView
import org.coner.style.ConerExtendedPalette
import org.coner.style.ConerFxStylesheet
import org.coner.style.ConerLogoPalette
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

class DrsStylesheet : Stylesheet(ConerFxStylesheet::class) {

    companion object {
        val logo by cssclass("logo")
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
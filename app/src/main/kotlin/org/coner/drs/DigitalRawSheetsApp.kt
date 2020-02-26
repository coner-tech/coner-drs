/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.coner.drs

import javafx.application.Application
import javafx.scene.image.Image
import javafx.stage.Stage
import net.harawata.appdirs.AppDirsFactory
import org.coner.drs.di.KatanaScopes
import org.coner.drs.di.numberFormatModule
import org.coner.drs.di.reportModule
import org.coner.drs.ui.DrsStylesheet
import org.coner.drs.ui.main.MainView
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import tornadofx.*
import java.io.File
import java.nio.file.Path

open class DigitalRawSheetsApp : App(
        primaryView = MainView::class,
        stylesheet = DrsStylesheet::class
), KatanaTrait {

    init {
        importStylesheet("/style/coner-unsafe.css")
    }
    override val configBasePath: Path = AppDirsFactory.getInstance()
            .getUserConfigDir(
                    "digital-raw-sheets",
                    "0.0.0",
                    "coner"
            ).let { userConfigDir: String -> File(userConfigDir).toPath() }

    override val component = Component(
            numberFormatModule,
            reportModule
    )

    val katanaScopes: KatanaScopes = KatanaScopes()

    open val forceExitOnStop = true

    override fun start(stage: Stage) {
        super.start(stage)
        println("DigitalRawSheetApp.start()")
        stage.icons.addAll(
                listOf(16, 32, 48, 64, 128, 256, 512, 1024)
                        .map { Image("/coner-icon/coner-icon_$it.png") }
        )
        val uiComponent = checkNotNull(stage.uiComponent<UIComponent>())
        stage.titleProperty().bind(
                stringBinding(uiComponent.titleProperty) {
                    "Coner Digital Raw Sheets - ${uiComponent.title}"
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
    Application.launch(DigitalRawSheetsApp::class.java, *args)
}


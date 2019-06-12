package org.coner.drs.it.util

import javafx.stage.Stage
import org.coner.drs.DigitalRawSheetApp
import tornadofx.*
import java.io.File
import java.nio.file.Path

class IntegrationTestApp(appConfigBasePath: Path) : DigitalRawSheetApp() {
    override val configBasePath = appConfigBasePath
    override val drsProperties = null
    override val forceExitOnStop = false
    override var scope = Scope()

    override fun start(stage: Stage) {
        super.start(stage)
        // for mysterious reasons, if the stage has a minHeight (great than 0), subsequent tests fail seemingly due
        // to the scene not appearing on the stage, and therefore nodes aren't visible or in bounds for the robot
        // to click on.
        stage.minHeight = 0.0
    }
}
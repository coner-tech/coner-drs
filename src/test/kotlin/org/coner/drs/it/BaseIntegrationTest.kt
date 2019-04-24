package org.coner.drs.it

import javafx.application.Platform
import javafx.scene.Scene
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.stage.StageStyle
import org.coner.drs.DigitalRawSheetApp
import org.coner.drs.test.page.fast.FastStartPage
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import tornadofx.*
import java.io.File
import java.nio.file.Files
import java.util.*

abstract class BaseIntegrationTest {

    protected var app: DigitalRawSheetApp? = null
    protected var robot: FxRobot? = null

    private var rootFolder: File? = null
    protected var appConfigBasePath: File? = null
    protected var rawSheetDatabase: File? = null
    protected var crispyFishDatabase: File? = null

    @BeforeEach
    private fun baseBeforeEach() {
        rootFolder = createTempFolder()
        appConfigBasePath = File(rootFolder, "appConfigBasePath").apply { mkdir() }
        rawSheetDatabase = File(rootFolder,"rawSheetDatabase").apply { mkdir() }
        crispyFishDatabase = File(rootFolder, "crispyFishDatabase").apply { mkdir() }
        FxToolkit.registerPrimaryStage()
        val app = IntegrationTestApp(
                appConfigBasePath = appConfigBasePath!!
        )
        this.app = app
        FxToolkit.setupApplication { app }
        this.robot = FxRobot()
        val startPage = FastStartPage(find(app.scope), robot!!)
        startPage.setRawSheetDatabase(rawSheetDatabase!!)
        startPage.setCrispyFishDatabase(crispyFishDatabase!!)
        startPage.clickStartButton()
    }

    private fun createTempFolder(): File {
        val prefix = "${javaClass.canonicalName}-${UUID.randomUUID()}"
        return Files.createTempDirectory(prefix).toFile()
    }

    @AfterEach
    private fun baseAfterEach() {
        println("BaseIntegrationTest.baseAfterEach()")
        FxToolkit.cleanupStages()
        FxToolkit.cleanupApplication(app)
        check(rootFolder?.deleteRecursively() ?: false) {
            "Failed to delete test root folder"
        }
        app = null
        robot = null
        rootFolder = null
        appConfigBasePath = null
        rawSheetDatabase = null
        crispyFishDatabase = null
    }

    private class IntegrationTestApp(appConfigBasePath: File) : DigitalRawSheetApp() {
        override val configBasePath = appConfigBasePath.toPath()
        override val drsProperties = null
    }
}
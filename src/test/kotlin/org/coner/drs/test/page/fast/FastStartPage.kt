package org.coner.drs.test.page.fast

import org.coner.drs.test.page.real.RealStartPage
import org.coner.drs.ui.start.StartModel
import org.testfx.api.FxRobot
import java.io.File
import java.nio.file.Path

class FastStartPage(
        private val startModel: StartModel,
        robot: FxRobot
) : RealStartPage(robot) {

    override fun setRawSheetDatabase(file: Path) {
        robot.interact {
            startModel.rawSheetDatabase = file
        }
    }

    override fun setCrispyFishDatabase(file: File) {
        robot.interact {
            startModel.crispyFishDatabase = file
        }
    }

    override fun clickStartButton() {
        robot.interact {
            startButton().fire()
        }
    }
}
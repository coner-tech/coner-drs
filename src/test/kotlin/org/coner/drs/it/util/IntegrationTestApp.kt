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

package org.coner.drs.it.util

import javafx.stage.Stage
import org.coner.drs.DigitalRawSheetApp
import tornadofx.*
import java.io.File
import java.nio.file.Path

class IntegrationTestApp(appConfigBasePath: Path) : DigitalRawSheetApp() {
    override val configBasePath = appConfigBasePath
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
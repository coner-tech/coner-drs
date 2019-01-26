package org.coner.drs.util

import javafx.scene.control.TextInputControl
import org.coner.drs.io.DrsIoController
import tornadofx.*
import java.io.File

fun TextInputControl.requireFileWithinCrispyFishDatabase() = validator(ValidationTrigger.OnChange()) {
    val io = find<DrsIoController>()
    if (!io.isInsideCrispyFishDatabase(File(it)))
        error("File must be inside Crispy Fish Database")
    else
        null
}
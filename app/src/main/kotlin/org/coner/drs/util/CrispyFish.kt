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

package org.coner.drs.util

import javafx.scene.control.TextInputControl
import org.coner.drs.ui.home.HomeModel
import tornadofx.*
import java.io.File

fun TextInputControl.requireFileWithinCrispyFishDatabase(crispyFishDatabase: File) = validator(ValidationTrigger.OnChange()) {
    if (!File(it).isInsideCrispyFishDatabase(crispyFishDatabase)) {
        error("File must be inside Crispy Fish Database")
    } else {
        null
    }
}

private fun File.isInsideCrispyFishDatabase(crispyFishDatabase: File, recursion: Int = 0): Boolean {
    check(recursion <= 1) { "Recursion exceeded maximum of 1" }
    return if (isAbsolute) {
        canonicalFile.startsWith(crispyFishDatabase)
    } else {
        crispyFishDatabase.resolve(this)
                .isInsideCrispyFishDatabase(
                        crispyFishDatabase = crispyFishDatabase,
                        recursion = recursion + 1
                )
    }
}
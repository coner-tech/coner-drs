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

package org.coner.drs.ui.start

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.io.File
import tornadofx.getValue
import tornadofx.setValue
import java.nio.file.Path
import java.nio.file.Paths

class StartModel : ViewModel() {
    val rawSheetDatabaseProperty = SimpleObjectProperty<Path>(
            this,
            "rawSheetDatabase",
            with(config) { if (containsKey("rawSheetDatabase")) Paths.get(string("rawSheetDatabase")) else null }
    )
    var rawSheetDatabase by rawSheetDatabaseProperty
    val crispyFishDatabaseProperty = SimpleObjectProperty<File>(
            this,
            "crispyFishDatabase",
            with(config) { if (containsKey("crispyFishDatabase")) File(string("crispyFishDatabase")) else null }
    )
    var crispyFishDatabase by crispyFishDatabaseProperty

    val subscriberProperty = SimpleBooleanProperty(
            this,
            "subscriber",
            config.boolean("subscriber", true)
    )
    var subscriber by subscriberProperty

    override fun onCommit() {
        with(config) {
            set("rawSheetDatabase" to rawSheetDatabase)
            set("crispyFishDatabase" to crispyFishDatabase.absolutePath)
            set("subscriber" to subscriber)
            save()
        }
    }
}
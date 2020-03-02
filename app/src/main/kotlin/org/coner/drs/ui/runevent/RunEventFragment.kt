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

package org.coner.drs.ui.runevent

import javafx.scene.layout.Region
import org.coner.drs.di.katanaScopes
import org.coner.drs.domain.entity.RunEvent
import org.rewedigital.katana.KatanaTrait
import tornadofx.*
import java.util.*

class RunEventFragment : Fragment("Run Event"), KatanaTrait {
    val eventId: UUID by param()
    val subscriber: Boolean by param()
    override val component: org.rewedigital.katana.Component by param()

    val model: RunEventModel by inject()
    val controller: RunEventController by inject()

    init {
        controller.init(eventId, subscriber)
    }

    override val root = borderpane {
        id = "run-event"
        top {
            add(find<RunEventTopView>())
        }
        left {
            add(find<AddNextDriverView>())
        }
        center {
            add(find<RunEventTableView>())
        }
        right { add(find<RunEventRightDrawerView>()) }
    }

    override fun onDock() {
        super.onDock()
        controller.docked()
    }

    override fun onUndock() {
        super.onUndock()
        controller.undocked()
    }

}
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

package org.coner.drs.ui.chooseevent

import io.reactivex.disposables.CompositeDisposable
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import org.coner.drs.domain.entity.Event
import tornadofx.*

class ChooseEventModel : ViewModel() {
    val events = observableListOf<Event>()
    val choiceProperty = SimpleObjectProperty<Event>(this, "event")
    var choice by choiceProperty

    val disposables = CompositeDisposable()

    val dockedProperty = SimpleBooleanProperty(this, "docked", false)
    var docked by dockedProperty

}
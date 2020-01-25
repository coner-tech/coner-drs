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

package org.coner.drs.ui.alterdriversequence

import com.github.thomasnield.rxkotlinfx.toObservable
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
import org.coner.drs.domain.payload.InsertDriverIntoSequenceResult
import org.coner.drs.domain.service.RunService
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class AlterDriverSequenceModel : ViewModel() {

    val eventProperty = SimpleObjectProperty<RunEvent>(this, "event")
    var event by eventProperty

    val sequenceProperty = SimpleIntegerProperty(this, "sequence")
    var sequence by sequenceProperty

    val registrationProperty = SimpleObjectProperty<Registration>(this, "registration")
    var registration by registrationProperty

    val relativeProperty = SimpleObjectProperty<InsertDriverIntoSequenceRequest.Relative>(this, "relative")
    var relative by relativeProperty

    val previewResultProperty = SimpleObjectProperty<InsertDriverIntoSequenceResult>(this, "previewResult")
    var previewResult by previewResultProperty

    val resultProperty = SimpleObjectProperty<InsertDriverIntoSequenceResult>(this, "result")
    var result by resultProperty

}

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

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.ObservableList
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class PreviewAlteredDriverSequenceResult(
        runs: ObservableList<Run>,
        insertedRun: Run?
) {
    val runsProperty = SimpleListProperty<Run>(this, "runs", runs)
    var runs by runsProperty

    val insertedProperty = SimpleObjectProperty<Run>(this, "inserted", insertedRun)
    var inserted by insertedProperty

    class Run(run: org.coner.drs.domain.entity.Run, status: Status) {
        val idProperty = SimpleObjectProperty(run.id)
        val id by idProperty

        val sequenceProperty = SimpleIntegerProperty(run.sequence)
        val sequence by sequenceProperty

        val statusProperty = SimpleObjectProperty(status)
        val status by statusProperty

        val numbersProperty = SimpleStringProperty(run.registrationNumbers)
        val numbers by numbersProperty

        val nameProperty = SimpleStringProperty(run.registrationDriverName)
        val name by nameProperty

        val carModelProperty = SimpleStringProperty(run.registrationCarModel)
        val carModel by carModelProperty

        val carColorProperty = SimpleStringProperty(run.registrationCarColor)
        val carColor by carColorProperty

        val rawTimeProperty = SimpleObjectProperty(run.rawTime)
        val rawTime by rawTimeProperty

        val compositePenaltyProperty = SimpleObjectProperty(run.compositePenalty)
        val compositePenalty by compositePenaltyProperty
    }

    enum class Status {
        SAME,
        INSERTED,
        SHIFTED
    }


}
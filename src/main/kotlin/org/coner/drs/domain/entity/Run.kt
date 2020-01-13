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

package org.coner.drs.domain.entity

import com.github.thomasnield.rxkotlinfx.toObservable
import javafx.beans.binding.Binding
import javafx.beans.property.*
import javafx.collections.ObservableList
import org.coner.drs.io.db.WatchedEntity
import tornadofx.*
import java.math.BigDecimal
import java.util.*
import tornadofx.getValue
import tornadofx.setValue

class Run(
        id: UUID = UUID.randomUUID(),
        event: Event,
        sequence: Int = 0,
        registration: Registration? = null,
        rawTime: BigDecimal? = null,
        cones: Int = 0,
        didNotFinish: Boolean = false,
        disqualified: Boolean = false,
        rerun: Boolean = false
): WatchedEntity<Run> {
    val idProperty = SimpleObjectProperty<UUID>(this, "id", id)
    var id by idProperty

    val eventProperty = SimpleObjectProperty<Event>(this, "event", event)
    var event by eventProperty

    val sequenceProperty = SimpleIntegerProperty(this, "sequence", sequence)
    var sequence by sequenceProperty

    val registrationProperty = SimpleObjectProperty<Registration>(this, "registration", registration)
    var registration by registrationProperty

    val registrationNumbersProperty = SimpleStringProperty(this, "registrationNumbers", "").apply {
        bind(registrationProperty.select { it.numbersProperty })
    }
    val registrationNumbers by registrationNumbersProperty

    val registrationDriverNameProperty = registrationProperty.select(Registration::nameProperty)
    val registrationDriverName by registrationDriverNameProperty

    val registrationCarModelProperty = registrationProperty.select(Registration::carModelProperty)
    val registrationCarModel by registrationCarModelProperty

    val registrationCarColorProperty = registrationProperty.select(Registration::carColorProperty)
    val registrationCarColor by registrationCarColorProperty

    val rawTimeProperty = SimpleObjectProperty<BigDecimal>(this, "rawTime", rawTime)
    var rawTime by rawTimeProperty

    val conesProperty = SimpleIntegerProperty(this, "cones", cones)
    var cones by conesProperty

    val didNotFinishProperty = SimpleBooleanProperty(this, "didNotFinish", didNotFinish)
    var didNotFinish by didNotFinishProperty

    val disqualifiedProperty = SimpleBooleanProperty(this, "disqualified", disqualified)
    var disqualified by disqualifiedProperty

    val rerunProperty = SimpleBooleanProperty(this, "rerun", rerun)
    var rerun by rerunProperty

    val compositePenaltyProperty = SimpleObjectProperty<CompositePenalty>(this, "compositePenalty", CompositePenalty(
            conesProperty = conesProperty,
            rerunProperty = rerunProperty,
            didNotFinishProperty = didNotFinishProperty,
            disqualifiedProperty = disqualifiedProperty
    ))
    val compositePenalty by compositePenaltyProperty

    override fun onWatchedEntityUpdate(updated: Run) {
        sequence = updated.sequence
        rawTime = updated.rawTime
        cones = updated.cones
        didNotFinish = updated.didNotFinish
        disqualified = updated.disqualified
        rerun = updated.rerun
    }

    class CompositePenalty(
            val conesProperty: SimpleIntegerProperty,
            val rerunProperty: SimpleBooleanProperty,
            val didNotFinishProperty: SimpleBooleanProperty,
            val disqualifiedProperty: SimpleBooleanProperty
    ) {
        val cones by conesProperty
        val rerun by rerunProperty
        val didNotFinish by didNotFinishProperty
        val disqualified by disqualifiedProperty
    }
}
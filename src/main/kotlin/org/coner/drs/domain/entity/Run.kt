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
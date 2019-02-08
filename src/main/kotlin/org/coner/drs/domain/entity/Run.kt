package org.coner.drs.domain.entity

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import tornadofx.*
import java.math.BigDecimal
import java.util.*

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
) {
    val idProperty = SimpleObjectProperty<UUID>(this, "id", id)
    var id by idProperty

    val eventProperty = SimpleObjectProperty<Event>(this, "event", event)
    var event by eventProperty

    val sequenceProperty = SimpleIntegerProperty(this, "sequence", sequence)
    var sequence by sequenceProperty

    val registrationProperty = SimpleObjectProperty<Registration>(this, "registration", registration)
    var registration by registrationProperty

    val registrationCategoryProperty = registrationProperty.select(Registration::categoryProperty)
    val registrationCategory by registrationCategoryProperty

    val registrationHandicapProperty = registrationProperty.select(Registration::handicapProperty)
    val registrationHandicap by registrationHandicapProperty

    val registrationNumberProperty = registrationProperty.select(Registration::numberProperty)
    val registrationNumber by registrationNumberProperty

    val registrationDriverNameProperty = registrationProperty.select(Registration::nameProperty)
    val registrationCarModelProperty = registrationProperty.select(Registration::carModelProperty)
    val registrationCarColorProperty = registrationProperty.select(Registration::carColorProperty)

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

}
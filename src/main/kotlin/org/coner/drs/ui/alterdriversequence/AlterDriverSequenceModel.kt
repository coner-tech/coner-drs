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


    private val service: RunService by inject()

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

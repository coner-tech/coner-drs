package org.coner.drs.ui.alterdriversequence

import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.schedulers.Schedulers
import javafx.concurrent.Task
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
import org.coner.drs.domain.payload.InsertDriverIntoSequenceResult
import org.coner.drs.domain.service.RunService
import org.coner.drs.ui.runevent.RunEventModel
import tornadofx.*

class AlterDriverSequenceController : Controller() {

    private val model: AlterDriverSequenceModel by inject()
    private val service: RunService by inject()

    init {
        with(find<SpecifyDriverSequenceAlterationModel>()) {
            model.registrationProperty.bindBidirectional(registrationProperty)
            model.relativeProperty.bindBidirectional(relativeProperty)
            model.sequenceProperty.bindBidirectional(sequenceProperty)
        }
        model.sequenceProperty.onChange { performDryRunForPreview() }
        model.registrationProperty.onChange { performDryRunForPreview() }
        model.relativeProperty.onChange { performDryRunForPreview() }
    }

    fun showAlterDriverSequenceAndWait(sequence: Int): InsertDriverIntoSequenceResult? {
        model.event = find<RunEventModel>().event
        model.sequence = sequence
        model.registration = null
        find<AlterDriverSequenceView>().openModal(block = true)
        return model.result
    }

    fun performDryRunForPreview() {
        val request = InsertDriverIntoSequenceRequest(
                event = model.event,
                runs = model.event.runs,
                registration = model.registration,
                sequence = model.sequence,
                relative = model.relative ?: InsertDriverIntoSequenceRequest.Relative.BEFORE,
                dryRun = true
        )
        service.insertDriverIntoSequence(request)
                .subscribeOn(Schedulers.computation())
                .observeOnFx()
                .subscribe { previewResult ->
                    model.previewResult = previewResult
                }
    }

    fun executeAlterDriverSequence(): InsertDriverIntoSequenceResult {
        val request = InsertDriverIntoSequenceRequest(
                event = model.event,
                runs = model.event.runs,
                registration = model.registration,
                sequence = model.sequence,
                relative = model.relative
        )
        return service.insertDriverIntoSequence(request).blockingGet()
    }
}

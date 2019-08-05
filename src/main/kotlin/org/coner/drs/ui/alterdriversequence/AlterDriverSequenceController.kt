package org.coner.drs.ui.alterdriversequence

import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.schedulers.Schedulers
import javafx.concurrent.Task
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.mapper.RunMapper
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
import org.coner.drs.domain.payload.InsertDriverIntoSequenceResult
import org.coner.drs.domain.service.RunService
import org.coner.drs.ui.runevent.RunEventModel
import tornadofx.*

class AlterDriverSequenceController : Controller() {

    private val model: AlterDriverSequenceModel by inject()
    private val service: RunService by inject()

    init {
        model.sequenceProperty.onChange { performDryRunForPreview() }
        model.registrationProperty.onChange { performDryRunForPreview() }
        model.relativeProperty.onChange { performDryRunForPreview() }
        model.previewResultProperty.onChange { result ->
            val model: PreviewAlteredDriverSequenceModel = find()
            model.previewResult = when {
                result != null -> RunMapper.toPreviewAlteredDriverSequenceResult(result)
                else -> PreviewAlteredDriverSequenceResult(observableListOf(), null)
            }
            model.previewResultRuns = model.previewResult.runs.sorted(compareByDescending(PreviewAlteredDriverSequenceResult.Run::sequence))
        }
    }

    fun showAlterDriverSequenceAndWait(sequence: Int, event: RunEvent): InsertDriverIntoSequenceResult? {
        model.event = event
        with(find<SpecifyDriverSequenceAlterationModel>()) {
            model.registrationProperty.bindBidirectional(registrationProperty)
            model.relativeProperty.bindBidirectional(relativeProperty)
            model.sequenceProperty.bindBidirectional(sequenceProperty)
        }
        model.sequence = sequence
        model.registration = null
        // deliberately don't reset `relative` to maintain user preference
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

package org.coner.drs.ui.alterdriversequence

import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.schedulers.Schedulers
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
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
    }

    fun showAlterDriverSequenceView(sequence: Int) {
        model.event = find<RunEventModel>().event
        model.sequence = sequence
        model.registration = null
        find<SpecifyDriverSequenceAlterationController>().reset()
        find<AlterDriverSequenceView>().openModal()
    }

    fun performDryRunForPreview() {
        val request = InsertDriverIntoSequenceRequest(
                event = model.event,
                runs = model.event.runs,
                registration = model.registration,
                sequence = model.sequence,
                relative = model.relative,
                dryRun = true
        )
        service.insertDriverIntoSequence(request)
                .subscribeOn(Schedulers.computation())
                .observeOnFx()
                .subscribe { previewResult ->
                    model.previewResult = previewResult
                }
    }

    fun executeAlterDriverSequence() {
        val request = InsertDriverIntoSequenceRequest(
                event = model.event,
                runs = model.event.runs,
                registration = model.registration,
                sequence = model.sequence,
                relative = model.relative
        )
        service.insertDriverIntoSequence(request).blockingGet()
    }
}

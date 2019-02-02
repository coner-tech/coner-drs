package org.coner.drs.ui.addevent

import org.coner.drs.domain.model.EventModel
import org.coner.drs.io.DrsIoController
import org.coner.drs.io.service.EventIoService
import tornadofx.*

class AddEventWizard : Wizard(
        title = "Add Event",
        heading = "Provide event information"
) {
    override val scope = super.scope as Scope

    val event: EventModel by inject()

    init {
        graphic = resources.imageview("/coner-icon/coner-icon_64.png")
        add<AddEventGeneralInformationStepFragment>()
        add<AddEventCrispyFishMetadataStepFragment>()
    }

    override val canFinish = allPagesComplete
    override val canGoNext = currentPageComplete

    class Scope(source: tornadofx.Scope) : tornadofx.Scope(
            FX.find<EventIoService>(source),
            FX.find<DrsIoController>(source)
    )
}


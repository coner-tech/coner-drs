package org.coner.drs.ui.addevent

import org.coner.drs.domain.model.EventModel
import org.coner.drs.io.DrsIoController
import tornadofx.*

class AddEventGeneralInformationStepFragment : Fragment("General Information") {
    val event: EventModel by inject()
    val io: DrsIoController by inject()

    override val root = form {
        fieldset(title) {
            field("Date") {
                datepicker(event.date) {
                    required()
                }
            }
            field("Name") {
                textfield(event.name) {
                    required()
                }
            }
        }
    }

    override val complete = event.valid(event.date, event.name)
}

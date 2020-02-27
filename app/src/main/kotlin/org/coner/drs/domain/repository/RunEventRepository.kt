package org.coner.drs.domain.repository

import org.coner.drs.di.katanaScopes
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.domain.mapper.EventMapper
import org.coner.drs.node.service.EventService
import org.rewedigital.katana.KatanaTrait
import tornadofx.*
import java.util.*

class RunEventRepository : Controller(), KatanaTrait {

    override val component = katanaScopes.runEvent.component

    private val service: EventService by component.inject()
    private val mapper: EventMapper by component.inject()
    private val runRepository: RunRepository by component.inject()

    fun getRunEventById(id: UUID): RunEvent {
        return requireNotNull(mapper.toRunEventUiEntity(service.findEventById(id))).apply {
            runs.setAll(runRepository.listRunsForEvent(this))
        }
    }
}
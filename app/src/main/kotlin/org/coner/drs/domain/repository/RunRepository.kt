package org.coner.drs.domain.repository

import org.coner.drs.di.katanaScopes
import org.coner.drs.domain.entity.Event
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.mapper.RunMapper
import org.coner.drs.node.service.RunService
import org.rewedigital.katana.KatanaTrait
import tornadofx.*
import java.util.*

class RunRepository : Controller(), KatanaTrait {

    override val component = katanaScopes.home.component

    private val service: RunService by component.inject()
    private val mapper = RunMapper

    fun listRunsForEvent(event: Event): List<Run> {
        return service.listRuns(eventId = event.id)
                .mapNotNull { mapper.toUiEntity(event, it) }
    }
}
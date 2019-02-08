package org.coner.drs.io.gateway

import org.coner.drs.domain.entity.Event
import org.coner.drs.io.crispyfish.buildEventControlFile
import tornadofx.*

class ClassDefinitionGateway : Controller() {

    fun listCategories(event: Event): List<String> {
        return event.buildEventControlFile().queryCategories().map { it.abbreviation }
    }

    fun listHandicaps(event: Event): List<String> {
        return event.buildEventControlFile().queryHandicaps().map { it.abbreviation }
    }
}
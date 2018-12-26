package org.coner.drs.io.service

import org.coner.drs.Event
import org.coner.drs.io.crispyfish.buildEventControlFile
import tornadofx.*

class ClassDefinitionService : Controller() {

    fun listCategories(event: Event): List<String> {
        return event.buildEventControlFile().queryCategories().map { it.abbreviation }
    }

    fun listHandicaps(event: Event): List<String> {
        return event.buildEventControlFile().queryHandicaps().map { it.abbreviation }
    }
}
package org.coner.drs.domain.controller

import org.coner.drs.domain.model.BuildProperties
import org.coner.drs.io.gateway.BuildPropertiesGateway
import tornadofx.*

class BuildPropertiesController : Controller() {

    val buildProperties: BuildProperties? = find<BuildPropertiesGateway>().load()

}
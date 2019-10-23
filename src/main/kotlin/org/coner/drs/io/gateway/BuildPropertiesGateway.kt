package org.coner.drs.io.gateway

import org.coner.drs.domain.model.BuildProperties
import tornadofx.*
import java.util.*

class BuildPropertiesGateway : Controller() {

    fun load(): BuildProperties? = try {
        resources.url("/build.properties").openStream().use {
            BuildProperties(
                    bundle = PropertyResourceBundle(it)
            )
        }
    } catch (t: Throwable) {
        null
    }
}
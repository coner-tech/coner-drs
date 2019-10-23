package org.coner.drs.domain.model

import java.util.*

class BuildProperties(private val bundle: PropertyResourceBundle) {
    val conerDrsVersion: String
        get() = bundle.getString("coner-drs.version")

}
package org.coner.drs.io.crispyfish

import org.coner.crispyfish.model.Registration

object RegistrationMapper {

    fun toUiEntity(crispyFishEntity: Registration) = with(crispyFishEntity) {
        org.coner.drs.domain.entity.Registration(
                category = category?.abbreviation ?: "",
                handicap = handicap.abbreviation,
                number = number,
                name = "$firstName $lastName",
                carModel = carModel,
                carColor = carColor
        )
    }
}
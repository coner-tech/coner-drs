package org.coner.drs.node.fixture

import org.coner.drs.node.db.entity.EventDbEntity
import java.time.LocalDate
import java.util.*

object Events {

    fun basic() = EventDbEntity(
            id = UUID.randomUUID(),
            date = LocalDate.of(2019, 3, 25),
            name = "basic",
            crispyFishMetadata = EventDbEntity.CrispyFishMetadata(
                    classDefinitionFile = "classDefinitionFile",
                    eventControlFile = "eventControlFile"
            )
    )

}
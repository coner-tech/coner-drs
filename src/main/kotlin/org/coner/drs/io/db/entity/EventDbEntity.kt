package org.coner.drs.io.db.entity

import org.coner.snoozle.db.entity.Entity
import java.time.LocalDate
import java.util.*

data class EventDbEntity(
        val id: UUID = UUID.randomUUID(),
        val date: LocalDate,
        val name: String,
        val crispyFishMetadata: CrispyFishMetadata

) : Entity {
    data class CrispyFishMetadata(
            val classDefinitionFile: String,
            val eventControlFile: String
    )
}


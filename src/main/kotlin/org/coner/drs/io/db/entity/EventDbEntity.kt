package org.coner.drs.io.db.entity

import org.coner.snoozle.db.Entity
import org.coner.snoozle.db.EntityPath
import java.time.LocalDate
import java.util.*

@EntityPath("/events/{id}")
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


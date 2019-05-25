package org.coner.drs.io.db.entity

import org.coner.snoozle.db.AutomaticVersionedEntity
import org.coner.snoozle.db.Entity
import org.coner.snoozle.db.EntityPath
import java.math.BigDecimal
import java.util.*

@EntityPath("/events/{eventId}/runs/{id}")
@AutomaticVersionedEntity
data class RunDbEntity(
        val id: UUID = UUID.randomUUID(),
        val eventId: UUID,
        val sequence: Int,
        val category: String = "",
        val handicap: String = "",
        val number: String = "",
        val rawTime: BigDecimal? = null,
        val cones: Int = 0,
        val didNotFinish: Boolean = false,
        val disqualified: Boolean = false,
        val rerun: Boolean = false
) : Entity


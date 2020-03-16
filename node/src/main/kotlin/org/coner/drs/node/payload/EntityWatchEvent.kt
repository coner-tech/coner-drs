package org.coner.drs.node.payload

import org.coner.snoozle.db.entity.EntityEvent
import java.util.*

data class EntityWatchEvent<E>(
        val entityEvent: EntityEvent<*>,
        val id: UUID,
        val entity: E?
)
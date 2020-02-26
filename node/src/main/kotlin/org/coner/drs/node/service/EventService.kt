package org.coner.drs.node.service

import org.coner.drs.node.db.DigitalRawSheetDatabase
import org.coner.drs.node.db.entity.EventDbEntity
import java.util.*

class EventService(
        private val database: DigitalRawSheetDatabase
) : EntityService<EventDbEntity> {

    override val resource = database.entity<EventDbEntity>()

    fun findEventById(id: UUID): EventDbEntity {
        return resource.get(id)
    }
}
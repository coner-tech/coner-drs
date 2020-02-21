package org.coner.drs.node.service

import org.coner.drs.node.db.DigitalRawSheetDatabase
import org.coner.drs.node.db.entity.EventDbEntity

class EventService(
        private val database: DigitalRawSheetDatabase
) : EntityService<EventDbEntity> {

    override val resource = database.entity<EventDbEntity>()
}
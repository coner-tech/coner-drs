package org.coner.drs.node.service

import io.reactivex.Observable
import org.coner.drs.node.db.DigitalRawSheetDatabase
import org.coner.drs.node.db.entity.EventDbEntity
import org.coner.drs.node.payload.EntityWatchEvent
import java.util.*

class EventService(
        private val database: DigitalRawSheetDatabase
) : EntityService<EventDbEntity> {

    override val resource = database.entity<EventDbEntity>()

    fun list(): List<EventDbEntity> {
        return resource.list()
    }

    fun watchList(): Observable<EntityWatchEvent<EventDbEntity>> {
        return resource.watchListing()
                .map { EntityWatchEvent(
                        entityEvent = it,
                        id = it.id,
                        entity = it.entity
                ) }
    }

    fun findEventById(id: UUID): EventDbEntity {
        return resource.get(id)
    }

    fun save(event: EventDbEntity) {
        resource.put(event)
    }

}
package org.coner.drs.node.service

import org.coner.snoozle.db.entity.Entity
import org.coner.snoozle.db.entity.EntityResource

interface EntityService<E : Entity> {

    val resource: EntityResource<E>

}
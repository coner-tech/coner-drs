package org.coner.drs.domain.model

import org.coner.drs.domain.entity.Event
import tornadofx.*

class EventModel : ItemViewModel<Event>(Event()) {
    val date = bind(Event::dateProperty, autocommit = true)
    val name = bind(Event::nameProperty, autocommit = true)
}
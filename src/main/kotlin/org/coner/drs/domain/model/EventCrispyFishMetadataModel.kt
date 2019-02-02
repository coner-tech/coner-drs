package org.coner.drs.domain.model

import org.coner.drs.domain.entity.Event
import tornadofx.*

class EventCrispyFishMetadataModel : ItemViewModel<Event.CrispyFishMetadata>(Event.CrispyFishMetadata()) {
    val eventControlFile = bind(Event.CrispyFishMetadata::eventControlFileProperty, autocommit = true)
    val classDefinitionFile = bind(Event.CrispyFishMetadata::classDefinitionFileProperty, autocommit = true)
}
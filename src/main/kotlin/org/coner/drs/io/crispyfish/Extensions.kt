package org.coner.drs.io.crispyfish

import org.coner.crispyfish.filetype.classdefinition.ClassDefinitionFile
import org.coner.crispyfish.filetype.ecf.EventControlFile
import org.coner.drs.Event

fun Event.buildEventControlFile() = EventControlFile(
        file = crispyFishMetadata.eventControlFile,
        classDefinitionFile = ClassDefinitionFile(crispyFishMetadata.classDefinitionFile),
        conePenalty = 2, // TODO: implement default value 2
        isTwoDayEvent = false // TODO: implement default value false
)
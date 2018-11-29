package org.coner.drs.db

import java.nio.file.WatchEvent
import java.util.*

data class EntityWatchEvent <T : Any>(val watchEvent: WatchEvent<*>, val id: UUID, val entity: T?)
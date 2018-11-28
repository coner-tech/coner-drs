package org.coner.drs.db

import java.nio.file.WatchEvent

data class MappedWatchEvent <T : Any>(val watchEvent: WatchEvent<*>, val entity: T?)
package org.coner.drs.di

import org.coner.drs.node.db.DigitalRawSheetDatabase
import org.coner.drs.node.service.EventService
import org.coner.drs.node.service.RunService
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.compact.singleton
import org.rewedigital.katana.dsl.get
import java.nio.file.Path

fun nodeModule(root: Path) = Module {
    singleton { DigitalRawSheetDatabase(root) }
    singleton { EventService(get()) }
    singleton { RunService(get()) }
}
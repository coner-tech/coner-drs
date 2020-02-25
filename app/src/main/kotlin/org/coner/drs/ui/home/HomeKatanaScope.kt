package org.coner.drs.ui.home

import org.coner.drs.di.nodeModule
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import java.nio.file.Path

class HomeKatanaScope(
        private val appComponent: Component,
        private val pathToDigitalRawSheetsDatabase: Path
) : KatanaTrait {
    override val component = Component(
            modules = listOf(
                    nodeModule(pathToDigitalRawSheetsDatabase)
            ),
            dependsOn = listOf(appComponent)
    )
}
package org.coner.drs.ui.home

import org.coner.drs.di.TornadoFxDisposableScope
import org.coner.drs.di.katanaAppComponent
import org.coner.drs.di.nodeModule
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.compact.singleton
import org.rewedigital.katana.dsl.get
import org.rewedigital.katana.inject
import tornadofx.*
import java.nio.file.Path

class HomeKatanaScope(
        fxComponent: tornadofx.Component,
        pathToDigitalRawSheetsDatabase: Path
) : KatanaTrait, TornadoFxDisposableScope {

    private val tornadoFxModule = Module {
        singleton { Scope() }
        singleton { fxComponent.find(scope = get(), componentType = HomeView::class.java) }
    }

    override val component = Component(
            modules = listOf(
                    tornadoFxModule,
                    nodeModule(pathToDigitalRawSheetsDatabase)
            ),
            dependsOn = listOf(fxComponent.katanaAppComponent)
    )

    override val tornadoFxScope: Scope by inject()

}
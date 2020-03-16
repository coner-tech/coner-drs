package org.coner.drs.ui.home

import org.coner.drs.di.TornadoFxDisposableScope
import org.coner.drs.di.katanaAppComponent
import org.coner.drs.di.nodeModule
import org.coner.drs.domain.mapper.EventMapper
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.compact.singleton
import org.rewedigital.katana.dsl.get
import tornadofx.*
import java.io.File
import java.nio.file.Path

class HomeKatanaScope(
        fxComponent: tornadofx.Component,
        pathToDigitalRawSheetsDatabase: Path,
        pathToCrispyFishDatabase: File
) : KatanaTrait, TornadoFxDisposableScope {

    private val tornadoFxModule = Module {
        singleton { FX.find<HomeView>(scope = tornadoFxScope).apply {
            find<HomeModel>().let { model ->
                model.pathToDigitalRawSheetsDatabase = pathToCrispyFishDatabase
                model.pathToCrispyFishDatabase = pathToCrispyFishDatabase
            }
        } }
    }

    private val mapperModule = Module {
        singleton { EventMapper(
                crispyFishDatabase = pathToCrispyFishDatabase
        ) }
    }

    override val component = Component(
            modules = listOf(
                    tornadoFxModule,
                    mapperModule,
                    nodeModule(pathToDigitalRawSheetsDatabase)
            ),
            dependsOn = listOf(fxComponent.katanaAppComponent)
    )

    override val tornadoFxScope = Scope()

}
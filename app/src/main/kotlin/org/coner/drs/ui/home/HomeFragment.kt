package org.coner.drs.ui.home

import org.coner.drs.di.KatanaScopes
import org.coner.drs.di.katanaAppComponent
import org.coner.drs.di.katanaScopes
import org.coner.drs.io.DrsIoController
import org.coner.drs.ui.chooseevent.ChooseEventView
import tornadofx.*
import java.io.File
import java.nio.file.Path

class HomeFragment : Fragment() {

    override val scope: Scope by param()

    private val model: HomeModel by inject()

    private val drsIo: DrsIoController by inject()

    override val root = stackpane()

    fun prepareDrsIo(pathToDrsDb: Path, pathToCfDb: File) {
        drsIo.open(
                pathToDrsDatabase = pathToDrsDb,
                pathToCrispyFishDatabase = pathToCfDb
        )
    }

    override fun onDock() {
        super.onDock()
        root.add(find<ChooseEventView>(scope))
    }

    override fun onUndock() {
        super.onUndock()
        root.replaceChildren()
        scope.deregister()
    }

    companion object {
        fun create(
                component: Component,
                pathToDigitalRawSheetsDatabase: Path
        ): HomeFragment {
            val fxScope = Scope()
            component.katanaScopes.home = HomeKatanaScope(
                    appComponent = component.katanaAppComponent,
                    pathToDigitalRawSheetsDatabase = pathToDigitalRawSheetsDatabase
            )
            return component.find(
                    params = mapOf(
                            HomeFragment::scope to fxScope
                    ),
                    scope = fxScope,
                    componentType = HomeFragment::class.java
            )
        }
    }
}


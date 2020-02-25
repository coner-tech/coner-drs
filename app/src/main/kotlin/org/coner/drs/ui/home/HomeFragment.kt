package org.coner.drs.ui.home

import org.coner.drs.di.KatanaScopes
import org.coner.drs.di.katanaAppComponent
import org.coner.drs.io.DrsIoController
import org.coner.drs.ui.chooseevent.ChooseEventView
import tornadofx.*
import java.io.File
import java.nio.file.Path

class HomeFragment : Fragment() {

    override val scope: Scope by param()

    val katanaScope: HomeKatanaScope by param()

    private val model: HomeModel by inject(scope)

    private val drsIo: DrsIoController by inject(scope)

    override val root = stackpane()

    fun prepareDrsIo(pathToDrsDb: Path, pathToCfDb: File) {
        drsIo.open(
                pathToDrsDatabase = pathToDrsDb,
                pathToCrispyFishDatabase = pathToCfDb
        )
    }

    override fun onDock() {
        super.onDock()
        model.katanaScope = katanaScope
        root.add(find<ChooseEventView>(scope))
    }

    override fun onUndock() {
        super.onUndock()
        root.replaceChildren()
        model.katanaScope = null
        scope.deregister()
    }

    companion object {
        fun create(
                component: Component,
                pathToDigitalRawSheetsDatabase: Path
        ): HomeFragment {
            check(KatanaScopes.home == null) { "KatanaScopes.home must be null. Something didn't clean up properly." }
            val fxScope = Scope()
            val katanaScope = HomeKatanaScope(
                    appComponent = component.katanaAppComponent,
                    pathToDigitalRawSheetsDatabase = pathToDigitalRawSheetsDatabase
            )
            return component.find(
                    params = mapOf(
                            HomeFragment::scope to fxScope,
                            HomeFragment::katanaScope to katanaScope
                    ),
                    scope = fxScope,
                    componentType = HomeFragment::class.java
            )
        }
    }
}


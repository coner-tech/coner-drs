package org.coner.drs.ui.home

import org.coner.drs.io.DrsIoController
import org.coner.drs.ui.chooseevent.ChooseEventView
import tornadofx.*
import java.io.File
import java.nio.file.Path

class HomeFragment : Fragment() {

    override val scope = Scope()

    val katanaScope: HomeScope by param()

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
        fun find(
                uiComponent: Component,
                katanaScope: HomeScope
        ): HomeFragment {
            return uiComponent.find(HomeFragment::katanaScope to katanaScope)
        }
    }
}


package org.coner.drs.ui.runevent

import org.coner.drs.di.TornadoFxDisposableScope
import org.coner.drs.di.katanaScopes
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.compact.singleton
import org.rewedigital.katana.inject
import tornadofx.*
import java.util.*

class RunEventKatanaScope(
        component: tornadofx.Component,
        eventId: UUID,
        subscriber: Boolean
) : KatanaTrait, TornadoFxDisposableScope {

    private val tornadoFxModule = Module {
        singleton { Scope() }
        singleton { component.find(
                params = mapOf(
                        RunEventFragment::eventId to eventId,
                        RunEventFragment::subscriber to subscriber,
                        RunEventFragment::component to component.katanaScopes.runEvent
                ),
                scope = tornadoFxScope,
                componentType = RunEventFragment::class.java
        ) }
    }

    override val component = Component(
            modules = listOf(
                    tornadoFxModule
            ),
            dependsOn = listOf(component.katanaScopes.home.component)
    )

    override val tornadoFxScope: Scope by inject()

}
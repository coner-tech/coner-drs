package org.coner.drs.ui.runevent

import org.coner.drs.di.TornadoFxDisposableScope
import org.coner.drs.di.katanaScopes
import org.coner.drs.domain.mapper.RunMapper
import org.coner.drs.domain.repository.RunRepository
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait
import org.rewedigital.katana.Module
import org.rewedigital.katana.dsl.compact.singleton
import tornadofx.*
import java.util.*

class RunEventKatanaScope(
        component: tornadofx.Component,
        eventId: UUID,
        subscriber: Boolean
) : KatanaTrait, TornadoFxDisposableScope {

    private val tornadoFxModule = Module {
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

    private val mapperModule = Module {
        singleton { RunMapper }
    }

    private val nodeModule = Module {
        singleton { RunRepository() }
    }

    override val component = Component(
            modules = listOf(
                    tornadoFxModule,
                    mapperModule,
                    nodeModule
            ),
            dependsOn = listOf(component.katanaScopes.home.component)
    )

    override val tornadoFxScope = Scope()

}
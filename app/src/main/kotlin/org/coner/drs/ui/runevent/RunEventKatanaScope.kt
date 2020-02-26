package org.coner.drs.ui.runevent

import org.coner.drs.di.KatanaScopes
import org.rewedigital.katana.Component
import org.rewedigital.katana.KatanaTrait

class RunEventKatanaScope(katanaScopes: KatanaScopes) : KatanaTrait {

    override val component = Component(
            modules = listOf(),
            dependsOn = listOf(katanaScopes.home.component)
    )
}
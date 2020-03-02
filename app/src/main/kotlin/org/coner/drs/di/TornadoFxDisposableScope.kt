package org.coner.drs.di

import tornadofx.*

interface TornadoFxDisposableScope {

    val tornadoFxScope: Scope

    fun dispose() {
        tornadoFxScope.deregister()
    }
}
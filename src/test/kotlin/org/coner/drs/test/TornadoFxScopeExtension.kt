package org.coner.drs.test

import org.junit.jupiter.api.extension.*
import tornadofx.*

class TornadoFxScopeExtension : BeforeEachCallback, AfterEachCallback, ParameterResolver {

    private var scope: Scope? = null

    override fun beforeEach(context: ExtensionContext?) {
        scope = Scope()
    }

    override fun afterEach(context: ExtensionContext?) {
        scope?.deregister()
        scope = null
    }

    override fun supportsParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Boolean {
        return when (parameterContext!!.parameter.type) {
            Scope::class.java -> true
            else -> false
        }
    }

    override fun resolveParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Any {
        return when (parameterContext!!.parameter.type) {
            Scope::class.java -> scope ?: Scope()
            else -> throw IllegalArgumentException()
        }
    }

}
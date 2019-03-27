package org.coner.drs.test

import org.junit.jupiter.api.extension.*
import tornadofx.*

class TornadoFxScopeExtension
    : BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback, ParameterResolver {

    override fun beforeAll(context: ExtensionContext?) {
        putScope(context!!)
    }

    override fun beforeEach(context: ExtensionContext?) {
    }

    override fun afterEach(context: ExtensionContext?) {
        getScope(context!!).apply {
            deregister()
        }
    }

    override fun afterAll(context: ExtensionContext?) {
        removeScope(context!!)
    }

    override fun supportsParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Boolean {
        return when (parameterContext!!.parameter.type) {
            Scope::class.java -> true
            else -> false
        }
    }

    override fun resolveParameter(parameterContext: ParameterContext?, extensionContext: ExtensionContext?): Any {
        return when (parameterContext!!.parameter.type) {
            Scope::class.java -> getScope(extensionContext!!)
            else -> throw IllegalArgumentException()
        }
    }

    private fun putScope(context: ExtensionContext) {
        context.getStore(ExtensionContext.Namespace.GLOBAL).put("scope", Scope())
    }

    private fun getScope(context: ExtensionContext): Scope {
        return context.getStore(ExtensionContext.Namespace.GLOBAL).get("scope") as Scope
    }

    private fun removeScope(context: ExtensionContext) {
        context.getStore(ExtensionContext.Namespace.GLOBAL).remove("scope")
    }

}
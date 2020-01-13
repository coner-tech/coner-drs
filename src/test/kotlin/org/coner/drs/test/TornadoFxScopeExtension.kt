/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
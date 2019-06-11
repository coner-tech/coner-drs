package org.coner.drs.test.extension

import tornadofx.View
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1

class ViewFixture(
        val inits: List<KFunction<*>>,
        val starts: List<KFunction<*>>,
        val stops: List<KFunction<*>>,
        val view: KProperty1<Any, View>?
)
package org.coner.drs.test.extension

import tornadofx.App
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1

class AppFixture(
        val init: KFunction<*>?,
        val setupApp: KFunction<*>,
        val start: KFunction<*>?,
        val app: KMutableProperty1<Any, App>?,
        val stop: KFunction<*>?
)
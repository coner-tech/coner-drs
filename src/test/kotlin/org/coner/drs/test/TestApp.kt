package org.coner.drs.test

import javafx.stage.Stage
import org.coner.drs.di.KatanaInjected
import org.rewedigital.katana.Component
import tornadofx.*
import kotlin.reflect.KClass

class TestApp(
        override val component: Component
) : App(), KatanaInjected {
}
package org.coner.drs.test.extension

import javafx.stage.Stage
import org.junit.jupiter.api.extension.*
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import tornadofx.*
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

class TornadoFxAppExtension : TestInstancePostProcessor,
        ParameterResolver,
        BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback  {

    private var ExtensionContext.robot: FxRobot?
        get() = getGlobal("robot")
        set(value) = putGlobal("robot", value)
    private var ExtensionContext.stage: Stage?
        get() = getGlobal("stage")
        set(value) = putGlobal("stage", value)
    private var ExtensionContext.app: tornadofx.App?
        get() = getGlobal("app")
        set(value) = putGlobal("app", value)
    private var ExtensionContext.fixture: AppFixture?
        get() = getGlobal("fixture")
        set(value) = putGlobal("fixture", value)

    private inline fun <reified T : Any> ExtensionContext.getGlobal(key: Any): T? {
        return getStore(ExtensionContext.Namespace.GLOBAL).get(key) as T?
    }

    private inline fun <reified T : Any> ExtensionContext.putGlobal(key: Any, value: T?) {
        getStore(ExtensionContext.Namespace.GLOBAL).put(key, value)
    }

    override fun postProcessTestInstance(testInstance: Any, context: ExtensionContext) {
        context.fixture = AppFixture(
                init = findInitFunction(testInstance),
                setupApp = findSetupAppFunction(testInstance),
                start = findStartFunction(testInstance),
                app = findAppProperty(testInstance),
                stop = findStopFunction(testInstance)
        )
    }

    private fun findInitFunction(testInstance: Any): KFunction<*>? {
        return testInstance::class.declaredMemberFunctions.singleOrNull { function ->
            function.findAnnotation<Init>() != null
        }?.apply {
            isAccessible = true
        }
    }

    private fun findSetupAppFunction(testInstance: Any): KFunction<*> {
        return testInstance::class.declaredMemberFunctions.singleOrNull { function ->
            function.returnType.isSubtypeOf(tornadofx.App::class.starProjectedType)
                    && function.findAnnotation<SetupApp>() != null
        }?.apply {
            isAccessible = true
        } ?: throw IllegalStateException("Test instance lacks @SetupApp function returning tornadofx.App")
    }

    private fun findAppProperty(testInstance: Any): KMutableProperty1<Any, tornadofx.App>? {
        return testInstance::class.declaredMemberProperties.singleOrNull { property ->
            property.returnType.isSubtypeOf(tornadofx.App::class.starProjectedType)
                    && property.findAnnotation<App>() != null
        }?.apply {
            isAccessible = true
        } as? KMutableProperty1<Any, tornadofx.App>?
    }

    private fun findStartFunction(testInstance: Any): KFunction<*>? {
        return testInstance::class.declaredMemberFunctions.singleOrNull { function ->
            function.findAnnotation<Start>() != null
        }?.apply {
            isAccessible = true
        }
    }

    private fun findStopFunction(testInstance: Any): KFunction<*>? {
        return testInstance::class.declaredMemberFunctions.singleOrNull { function ->
            function.findAnnotation<Stop>() != null
        }?.apply {
            isAccessible = true
        }
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return when (parameterContext.parameter.type) {
            FxRobot::class.java -> true
            Stage::class.java -> true
            else -> false
        }
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return when(parameterContext.parameter.type) {
            FxRobot::class.java -> extensionContext.robot!!
            Stage::class.java -> extensionContext.stage!!
            else -> false
        }
    }

    override fun beforeAll(context: ExtensionContext) {
    }

    override fun beforeEach(context: ExtensionContext) {
        val fixture = context.fixture!!
        context.stage = FxToolkit.registerPrimaryStage()
        context.robot = FxRobot()
        fixture.init?.run {
            FX.runAndWait { call(context.testInstance.get()) }
        }
        val app = FxToolkit.setupApplication {
            fixture.setupApp.call(context.testInstance.get()) as tornadofx.App
        } as tornadofx.App
        context.app = app
        fixture.app?.set(context.testInstance.get(), app)
        fixture.start?.run {
            call(context.testInstance.get(), context.robot!!)
        }
    }

    override fun afterEach(context: ExtensionContext) {
        val fixture = context.fixture!!
        fixture.stop?.call(context.testInstance.get())
        FX.runAndWait { context.stage!!.close() }
        FxToolkit.cleanupApplication(context.app!!)
        FxToolkit.cleanupStages()
        context.app = null
        context.stage = null
        context.robot = null
    }

    override fun afterAll(context: ExtensionContext) {
        context.fixture = null
    }

}

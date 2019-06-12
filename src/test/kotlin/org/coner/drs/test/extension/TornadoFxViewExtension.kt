package org.coner.drs.test.extension

import javafx.scene.Scene
import javafx.stage.Stage
import org.junit.jupiter.api.extension.*
import org.testfx.api.FxRobot
import org.testfx.api.FxToolkit
import tornadofx.*
import tornadofx.App
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*
import kotlin.reflect.jvm.isAccessible

class TornadoFxViewExtension : TestInstancePostProcessor,
        ParameterResolver,
        BeforeAllCallback, BeforeEachCallback, AfterEachCallback, AfterAllCallback  {

    private var ExtensionContext.robot: FxRobot?
        get() = getGlobal("robot")
        set(value) = putGlobal("robot", value)
    private var ExtensionContext.stage: Stage?
        get() = getGlobal("stage")
        set(value) = putGlobal("stage", value)
    private var ExtensionContext.scope: Scope?
        get() = getGlobal("scope")
        set(value) = putGlobal("scope", value)
    private var ExtensionContext.app: tornadofx.App?
        get() = getGlobal("app")
        set(value) = putGlobal("app", value)
    private var ExtensionContext.view: tornadofx.View?
        get() = getGlobal("view")
        set(value) = putGlobal("view", value)
    private var ExtensionContext.fixture: ViewFixture?
        get() = getGlobal("fixture")
        set(value) = putGlobal("fixture", value)

    private inline fun <reified T : Any> ExtensionContext.getGlobal(key: Any): T? {
        return getStore(ExtensionContext.Namespace.GLOBAL).get(key) as T?
    }

    private inline fun <reified T : Any> ExtensionContext.putGlobal(key: Any, value: T?) {
        getStore(ExtensionContext.Namespace.GLOBAL).put(key, value)
    }

    override fun postProcessTestInstance(testInstance: Any, context: ExtensionContext) {
        context.fixture = ViewFixture(
                init = findInitFunction(testInstance),
                start = findStartFunction(testInstance),
                stop = findStopFunction(testInstance),
                view = findViewProperty(testInstance)
        )
    }

    private fun findInitFunction(testInstance: Any): KFunction<*>? {
        return testInstance::class.declaredMemberFunctions.singleOrNull { function ->
            function.findAnnotation<Init>() != null
        }?.apply {
            isAccessible = true
        }
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

    private fun findViewProperty(testInstance: Any): KProperty1<Any, tornadofx.View>? {
        return testInstance::class.declaredMemberProperties.singleOrNull { property ->
            property.findAnnotation<View>() != null
        }?.apply {
            check(returnType.isSubtypeOf(tornadofx.View::class.starProjectedType)) {
                "Property annotated with @View is not a subtype of tornadofx.View"
            }
            isAccessible = true
        } as? KProperty1<Any, tornadofx.View>?
    }


    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return when (parameterContext.parameter.type) {
            FxRobot::class.java -> true
            Scope::class.java -> true
            App::class.java -> true
            Stage::class.java -> true
            else -> false
        }
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return when(parameterContext.parameter.type) {
            FxRobot::class.java -> extensionContext.robot!!
            Scope::class.java -> extensionContext.scope!!
            App::class.java -> extensionContext.app!!
            Stage::class.java -> extensionContext.stage!!
            else -> false
        }
    }

    override fun beforeAll(context: ExtensionContext) {
    }

    override fun beforeEach(context: ExtensionContext) {
        context.scope = Scope()
        val fixture = context.fixture
        context.stage = FxToolkit.registerPrimaryStage()
        context.robot = FxRobot()
        fixture?.init?.run { context.robot!!.interact { call(context.testInstance.get(), context.scope) } }
        context.view = fixture?.view?.get(context.testInstance.get())
        context.app = FxToolkit.setupApplication { object : tornadofx.App() {
            override var scope = context.scope!!
        } } as App
        val start = fixture?.start
        if (start != null) {
            context.robot!!.interact { start.call(context.testInstance.get(), context.stage!!) }
        } else {
            context.robot!!.interact {
                context.stage!!.scene = Scene(context.view!!.root)
                context.stage!!.show()
            }
        }
    }

    override fun afterEach(context: ExtensionContext) {
        context.fixture?.stop?.run { context.robot!!.interact { call(context.testInstance.get()) } }
        FxToolkit.cleanupApplication(context.app!!)
        FxToolkit.cleanupStages()
        context.view = null
        context.scope = null
        context.app = null
        context.stage = null
        context.robot = null
    }

    override fun afterAll(context: ExtensionContext) {
        context.fixture = null
    }

}

package org.coner.rs

import tornadofx.*
import java.io.File

class MainView : View() {
    val controller: MainController by inject()

    override val root = stackpane {  }

    init {
        root.add<StartView>()
        subscribe<ChangeToScreenEvent> { onChangeToScreen(it) }
    }

    fun onChangeToScreen(event: ChangeToScreenEvent) {
        val view = controller.findUIComponentForScreen(event.screen)
        root.replaceChildren(view)
    }
}

class MainController : Controller() {

    fun findUIComponentForScreen(screen: Screen): UIComponent = when(screen) {
        is Screen.Start -> find<StartView>()
        is Screen.ChooseEvent -> find<ChooseEventView>()
        is Screen.RunEvent -> find<RunEventFragment>(RunEventFragment::event to screen.event)
    }
}

sealed class Screen {
    object Start : Screen()
    class ChooseEvent(val dir: File) : Screen()
    class RunEvent(val event: Event) : Screen()
}

class ChangeToScreenEvent(val screen: Screen) : FXEvent()
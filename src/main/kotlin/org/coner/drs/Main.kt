package org.coner.drs

import javafx.beans.property.SimpleObjectProperty
import org.coner.drs.io.DrsIoController
import tornadofx.*
import java.io.File
import tornadofx.getValue
import tornadofx.setValue

class MainView : View() {
    val controller: MainController by inject()
    val model: MainModel by inject()

    override val root = stackpane {  }

    init {
        root.add<StartView>()
        model.screen = Screen.Start
        subscribe<ChangeToScreenEvent> { onChangeToScreen(it) }
    }

    fun onChangeToScreen(event: ChangeToScreenEvent) {
        val view = controller.onChangeToScreen(event.screen)
        root.replaceChildren(view)
    }
}

class MainController : Controller() {
    val model: MainModel by inject()
    val drsIo: DrsIoController by inject()

    fun onChangeToScreen(screen: Screen): UIComponent {
        val uiComponent = when (screen) {
            is Screen.Start -> find<StartView>()
            is Screen.ChooseEvent -> {
                if (model.screen == Screen.Start) {
                    drsIo.open(screen.dir)
                }
                find<ChooseEventView>()
            }
            is Screen.RunEvent -> find<RunEventFragment>(RunEventFragment::event to screen.event)
        }
        model.screen = screen
        return uiComponent
    }
}

class MainModel : ViewModel() {
    val screenProperty = SimpleObjectProperty<Screen>(this, "screen")
    var screen by screenProperty
}

sealed class Screen {
    object Start : Screen()
    class ChooseEvent(val dir: File) : Screen()
    class RunEvent(val event: Event) : Screen()
}

class ChangeToScreenEvent(val screen: Screen) : FXEvent()
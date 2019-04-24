package org.coner.drs.ui.main

import javafx.beans.property.SimpleBooleanProperty
import javafx.event.EventHandler
import tornadofx.*
import java.util.*

class MainView : View() {
    val controller: MainController by inject()
    val model: MainModel by inject()
    private val id = UUID.randomUUID()
    override val root = stackpane {  }

    init {
        println("MainView#${id}.init()")
    }

    fun onChangeToScreen(event: ChangeToScreenEvent) {
        println("MainView#${id} onChangeToScreen: ${event.screen}")
        val view = controller.onChangeToScreen(event.screen)
        if (root.children.isEmpty()) {
            println("Adding ${view.javaClass.simpleName}")
            root.add(view)
        } else {
            println("Replacing children with ${view.javaClass.simpleName}")
            root.replaceChildren(view)
        }
        titleProperty.unbind()
        titleProperty.bind(view.titleProperty)
    }

    override fun onDock() {
        super.onDock()
        println("MainView#${id}.onDock()")
        onChangeToScreen(ChangeToScreenEvent(Screen.Start))
        subscribe(action = onChangeToScreenHandler)
    }

    override fun onUndock() {
        super.onUndock()
        println("MainView#${id}.onUndock()")
        unsubscribe(onChangeToScreenHandler)
    }

    private val onChangeToScreenHandler: EventContext.(ChangeToScreenEvent) -> Unit = {
        onChangeToScreen(it)
    }

}

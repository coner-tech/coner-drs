package org.coner.drs.ui.main

import tornadofx.*

class MainView : View() {
    val controller: MainController by inject()
    val model: MainModel by inject()

    override val root = stackpane {  }

    init {
        onChangeToScreen(ChangeToScreenEvent(Screen.Start))
        subscribe<ChangeToScreenEvent> { onChangeToScreen(it) }
    }

    fun onChangeToScreen(event: ChangeToScreenEvent) {
        val view = controller.onChangeToScreen(event.screen)
        if (root.children.isEmpty()) {
            root.add(view)
        } else {
            root.replaceChildren(view)
        }
        titleProperty.unbind()
        titleProperty.bind(view.titleProperty)
    }
}
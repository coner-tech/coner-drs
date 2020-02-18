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
        root.replaceChildren()
    }

    private val onChangeToScreenHandler: EventContext.(ChangeToScreenEvent) -> Unit = {
        onChangeToScreen(it)
    }

}

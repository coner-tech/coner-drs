package org.coner.drs.util.tornadofx

import javafx.scene.control.ListView
import javafx.scene.control.SelectionMode
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import tornadofx.*

/**
 * Capture up and down button presses from the passed TextField while it has
 * focus and make selection changes on this ListView.
 */
fun <T : Any> ListView<T>.takeVerticalArrowKeyPressesAsSelectionsFrom(textField: TextField) {
    textField.setOnKeyPressed {
        check(selectionModel.selectionMode == SelectionMode.SINGLE) {
            "Only supports single selection mode"
        }
        if (items.isNullOrEmpty()) {
            return@setOnKeyPressed
        }
        when (it.code) {
            KeyCode.DOWN, KeyCode.UP -> {
                if (selectionModel.selectedIndex >= 0) {
                    if (it.code == KeyCode.DOWN && selectionModel.selectedIndex < items.lastIndex) {
                        selectionModel.selectNext()
                    } else if (it.code == KeyCode.UP && selectionModel.selectedIndex > 0) {
                        selectionModel.selectPrevious()
                    }
                } else {
                    selectionModel.select(0)
                }
                it.consume()
                scrollTo(selectedItem)
            }
            else -> { /* no-op */ }
        }
    }
}
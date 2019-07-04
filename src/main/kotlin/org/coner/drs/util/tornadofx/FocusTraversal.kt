package org.coner.drs.util.tornadofx

import javafx.scene.Node
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent

fun Node.overrideFocusTraversal(
        next: (() -> Node)? = null,
        previous: (() -> Node)? = null
) = addEventFilter(KeyEvent.KEY_PRESSED) { keyEvent ->
    if (keyEvent.code != KeyCode.TAB
            || keyEvent.isAltDown
            || keyEvent.isControlDown
            || keyEvent.isMetaDown
            || keyEvent.isShortcutDown
    ) {
        return@addEventFilter
    }
    val target = if (!keyEvent.isShiftDown)
        next?.invoke()
    else
        previous?.invoke()
    target?.run {
        keyEvent.consume()
        requestFocus()
    }
}
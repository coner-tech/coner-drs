package org.coner.drs.test.testfx

import javafx.scene.control.ChoiceBox
import javafx.scene.control.Label
import javafx.scene.control.Labeled
import javafx.scene.input.KeyCode
import org.hamcrest.Matcher
import org.testfx.api.FxRobot

inline fun <reified T> FxRobot.chooseInChoiceBox(
        choiceBox: ChoiceBox<T>,
        matcher: Matcher<Labeled>,
        maxAttempts: Int? = null
) {
    var matched = false
    val max = maxAttempts ?: choiceBox.items.size + 1
    var attempts = 0
    val label = from(choiceBox).lookup(".label").query<Label>()
    while (!matched) {
        if (attempts > max) {
            throw IllegalStateException("Exceeded max attempts")
        }
        clickOn(choiceBox)
        type(KeyCode.DOWN)
        type(KeyCode.ENTER)
        matched = matcher.matches(label)
        attempts++
    }
}
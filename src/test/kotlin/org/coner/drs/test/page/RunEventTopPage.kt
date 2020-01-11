package org.coner.drs.test.page

import javafx.scene.control.Label
import javafx.scene.layout.HBox

interface RunEventTopPage {
    fun root(): HBox
    fun eventName(): Label
}
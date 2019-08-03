package org.coner.drs.test.page

import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.TextField
import java.nio.file.Path
import kotlin.reflect.KClass

interface TimerConfigurationPage {
    fun root(): Node
    fun typeChoiceBox(): ChoiceBox<KClass<*>>
    fun chooseType(timerConfiguration: KClass<*>)
    fun applyButton(): Button
    fun pressApply()
    fun fileTextField(): TextField
    fun chooseFile(path: Path)
}

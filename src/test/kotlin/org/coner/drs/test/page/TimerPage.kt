package org.coner.drs.test.page

import javafx.scene.Node
import javafx.scene.control.Button

interface TimerPage {
    fun root(): Node
    fun configureButton(): Button
    fun pressConfigure()
    fun toTimerConfigurationPage(): TimerConfigurationPage
    fun startButton(): Button
    fun stopButton(): Button
    fun pressStart()
    fun pressStop()
}
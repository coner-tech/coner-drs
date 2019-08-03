package org.coner.drs.test.page

import tornadofx.*

interface RunEventRightDrawerPage {
    fun root(): Drawer
    fun timerDrawerItem(): DrawerItem
    fun expandTimer()
    fun collapseTimer()
    fun toTimerPage(): TimerPage
}

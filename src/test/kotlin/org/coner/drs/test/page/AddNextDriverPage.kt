package org.coner.drs.test.page

import javafx.scene.control.ListView
import javafx.scene.control.MenuItem
import javafx.scene.control.SplitMenuButton
import javafx.scene.control.TextInputControl
import org.coner.drs.domain.entity.Registration
import tornadofx.*

interface AddNextDriverPage {

    fun root(): Form

    fun numbersField(): TextInputControl

    fun writeInNumbersField(s: String)

    fun registrationsListView(): ListView<Registration>

    fun selectRegistration(registration: Registration)

    fun focusNumbersField()

    fun addButton(): SplitMenuButton

    fun doAddSelectedRegistration()

    fun addForceExactNumbersItem(): MenuItem

    fun doAddForceExactNumbers()
}
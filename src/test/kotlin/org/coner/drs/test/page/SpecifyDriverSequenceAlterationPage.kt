package org.coner.drs.test.page

import javafx.scene.control.ListView
import javafx.scene.control.TextInputControl
import javafx.scene.control.ToggleButton
import org.coner.drs.domain.entity.Registration
import tornadofx.*

interface SpecifyDriverSequenceAlterationPage {
    fun root(): Form
    fun relativeBefore(): ToggleButton
    fun relativeAfter(): ToggleButton
    fun selectRelativeBefore()
    fun selectRelativeAfter()
    fun numbersField(): TextInputControl
    fun focusNumbersField()
    fun writeInNumbersField(s: String)
    fun registrationsListView(): ListView<Registration>
    fun selectRegistration(registration: Registration)
}
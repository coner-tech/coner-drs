package org.coner.drs.ui.runevent

import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.input.KeyCombination
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.coner.drs.domain.entity.Registration
import org.coner.drs.ui.RegistrationCellFragment
import org.coner.drs.util.UpperCaseTextFormatter
import org.coner.drs.util.tornadofx.overrideFocusTraversal
import org.coner.drs.util.tornadofx.takeVerticalArrowKeyPressesAsSelectionsFrom
import tornadofx.*

class AddNextDriverView : View("Add Next Driver") {
    private val model: AddNextDriverModel by inject()
    private val controller: AddNextDriverController = find()
    private val runEventModel: RunEventModel by inject()

    override val root = form {
        id = "add-next-driver"
        prefWidth = 300.0
        fieldset(text = title, labelPosition = Orientation.VERTICAL) {
            vgrow = Priority.ALWAYS
            field(text = "Sequence", orientation = Orientation.VERTICAL) {
                textfield(runEventModel.event.runForNextDriverProperty.select { it.sequenceProperty }) {
                    id = "sequence"
                    isEditable = false
                }
            }
            var numbersField by singleAssign<TextField>()
            field(text = "Numbers", orientation = Orientation.VERTICAL) {
                vgrow = Priority.ALWAYS
                (inputContainer as VBox).spacing = 0.0
                textfield(model.numbersFieldProperty) {
                    numbersField = this
                    id = "numbers"
                    textFormatter = UpperCaseTextFormatter()
                    label.labelFor = this
                    overrideFocusTraversal(
                            next = controller.locateRunEventTable,
                            previous = controller.locateRunEventTable
                    )
                }
                listview<Registration> {
                    id = "registrations-list-view"
                    vgrow = Priority.ALWAYS
                    model.registrationList.bindTo(this)
                    cellFragment(RegistrationCellFragment::class)
                    bindSelected(model.registrationListSelectionProperty)
                    takeVerticalArrowKeyPressesAsSelectionsFrom(numbersField)
                    model.registrationListAutoSelectionCandidateProperty.onChange {
                        runLater {
                            selectionModel.select(it?.registration)
                            scrollTo(it?.registration)
                        }
                    }
                    onDoubleClick { controller.addNextDriverFromRegistrationListSelection() }
                    shortcut("Enter") {
                        if (!this.isFocused) return@shortcut
                        if (this.selectedItem == null) return@shortcut
                        controller.addNextDriverFromRegistrationListSelection()
                    }
                    overrideFocusTraversal(
                            next = controller.locateRunEventTable
                    )
                }
            }
            hbox {
                alignment = Pos.TOP_RIGHT
                splitmenubutton("Add") {
                    id = "add"
                    item(name = "Force Exact Numbers", keyCombination = KeyCombination.keyCombination("Ctrl+Enter")) {
                        id = "force-exact-numbers"
                        enableWhen(model.numbersFieldContainsNumbersTokensBinding)
                        action { controller.addNextDriverForceExactNumbers() }
                    }
                    enableWhen(
                            model.numbersFieldContainsNumbersTokensBinding
                                    .or(model.registrationListSelectionProperty.isNotNull)
                    )
                    action { controller.addNextDriverFromRegistrationListSelection() }
                    shortcut(KeyCombination.keyCombination("Enter")) {
                        if (model.registrationListSelectionProperty.isNull.get()) return@shortcut
                        controller.addNextDriverFromRegistrationListSelection()
                    }
                }
            }
        }
    }

    val numbersField: TextField
        get() = root.lookup("#numbers") as TextField
    val registrationListView: ListView<Registration>
        get() = root.lookup("#registrations-list-view") as ListView<Registration>

}
package org.coner.drs.ui.alterdriversequence

import javafx.geometry.Orientation
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.payload.InsertDriverIntoSequenceRequest
import org.coner.drs.ui.RegistrationCellFragment
import org.coner.drs.util.UpperCaseTextFormatter
import org.coner.drs.util.tornadofx.takeVerticalArrowKeyPressesAsSelectionsFrom
import tornadofx.*

class SpecifyDriverSequenceAlterationView : View() {

    private val model: SpecifyDriverSequenceAlterationModel by inject()
    private val controller: SpecifyDriverSequenceAlterationController = find()

    private lateinit var numbersTextField: TextField

    override val root = form {
        prefWidth = 300.0
        fieldset(text = "Specify Sequence Alteration", labelPosition = Orientation.VERTICAL) {
            field("Sequence", orientation = Orientation.VERTICAL) {
                textfield(model.sequenceProperty) {
                    isEditable = false
                }
            }
            field("Relative", orientation = Orientation.VERTICAL) {
                togglegroup {
                    hbox {
                        togglebutton(
                                text = "Before",
                                value = InsertDriverIntoSequenceRequest.Relative.BEFORE,
                                group = this@togglegroup
                        )
                        togglebutton(
                                text = "After",
                                value = InsertDriverIntoSequenceRequest.Relative.AFTER,
                                group = this@togglegroup
                        )
                    }
                    bind(model.relativeProperty)
                }
            }
            field("_Numbers", orientation = Orientation.VERTICAL) {
                (inputContainer as VBox).spacing = 0.0
                textfield(model.numbersFieldProperty) {
                    numbersTextField = this
                    label.labelFor = this
                    label.isMnemonicParsing = true
                    required()
                    textFormatter = UpperCaseTextFormatter()
                }
                listview<Registration> {
                    id = "registrations-list-view"
                    vgrow = Priority.ALWAYS
                    model.registrationList.bindTo(this)
                    cellFragment(RegistrationCellFragment::class)
                    bindSelected(model.registrationProperty)
                    takeVerticalArrowKeyPressesAsSelectionsFrom(numbersTextField)
                    model.registrationListAutoSelectionCandidateProperty.onChange {
                        runLater {
                            selectionModel.select(it?.registration)
                            scrollTo(it?.registration)
                        }
                    }
                    subscribe<ResetEvent> {
                        selectionModel.clearSelection()
                        scrollTo(0)
                    }
                }
            }
        }
    }

}

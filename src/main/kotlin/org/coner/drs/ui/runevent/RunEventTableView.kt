package org.coner.drs.ui.runevent

import javafx.scene.control.TableView
import javafx.scene.input.KeyCombination
import javafx.scene.layout.Priority
import org.coner.drs.DrsStylesheet
import org.coner.drs.di.KatanaInjected
import org.coner.drs.di.NumberFormatNames
import org.coner.drs.di.katanaApp
import org.coner.drs.di.numberFormatModule
import org.coner.drs.domain.entity.Run
import org.coner.drs.util.tornadofx.overrideFocusTraversal
import org.rewedigital.katana.Component
import tornadofx.*
import java.text.NumberFormat
import java.util.*

class RunEventTableView : View(), KatanaInjected {
    val model: RunEventTableModel by inject()
    val controller: RunEventTableController = find()
    override val component = Component(katanaApp.component)
    private val runTimeFormat by component.inject<NumberFormat>(NumberFormatNames.RUN_TIME)

    override val root = form {
        id = "run-event-table"
        fieldset("Runs") {
            vgrow = Priority.ALWAYS
            tableview(model.runsSortedBySequence) {
                id = "runs-table"
                legend.labelFor = this
                isEditable = false
                setSortPolicy { false }
                vgrow = Priority.ALWAYS
                column("Sequence", Run::sequenceProperty)
                column("Numbers", Run::registrationNumbersProperty)
                column("Name", Run::registrationDriverNameProperty)
                column("Car Model", Run::registrationCarModelProperty)
                column("Car Color", Run::registrationCarColorProperty)
                column("Time", Run::rawTimeProperty) {
                    cellFormat {
                        graphic = label(runTimeFormat.format(it))
                    }
                }
                column("Penalties", Run::compositePenaltyProperty) {
                    cellFormat { penalties ->
                        graphic = flowpane {
                            addClass(DrsStylesheet.penalties)
                            text(penalties.disqualifiedProperty.stringBinding { if (it == true) "Disqualified" else null }) {
                                addClass(Stylesheet.text)
                                managedWhen { textProperty().isNotNull }
                            }
                            text(penalties.didNotFinishProperty.stringBinding { if (it == true) "Did Not Finish" else null }) {
                                addClass(Stylesheet.text)
                                managedWhen { textProperty().isNotNull }
                                strikethroughProperty().bind(
                                        penalties.disqualifiedProperty
                                )
                            }
                            text(penalties.rerunProperty.stringBinding { if (it == true) "Re-Run" else null }) {
                                addClass(Stylesheet.text)
                                managedWhen { textProperty().isNotNull }
                                strikethroughProperty().bind(
                                        penalties.disqualifiedProperty
                                                .or(penalties.didNotFinishProperty)
                                )
                            }
                            text(penalties.conesProperty.stringBinding { if (it?.toInt() ?: -1 > 0) "+$it" else null }) {
                                addClass(Stylesheet.text)
                                managedWhen { textProperty().isNotNull }
                                strikethroughProperty().bind(
                                        penalties.disqualifiedProperty
                                                .or(penalties.didNotFinishProperty)
                                                .or(penalties.rerunProperty)
                                )
                            }
                        }
                    }
                }
                smartResize()
                contextmenu {
                    menu("Driver") {
                        item(
                                name = "Change Driver",
                                keyCombination = KeyCombination.keyCombination("Ctrl+D")
                        ) {
                            enableWhen(selectionModel.selectedItemProperty().isNotNull)
                            action {
                                val run = selectedItem ?: return@action
                                controller.showChangeDriver(run)
                            }
                        }
                        item(
                                name = "Insert Driver Into Sequence",
                                keyCombination = KeyCombination.keyCombination("Ctrl+Insert")
                        ) {
                            enableWhen(selectionModel.selectedItemProperty().isNotNull)
                            action { controller.showInsertDriver(selectedItem ?: return@action) }
                        }
                    }
                    menu("Time") {
                        item(
                                name = "Clear Time",
                                keyCombination = KeyCombination.keyCombination("Ctrl+C")
                        ) {
                            enableWhen(selectionModel.selectedItemProperty().isNotNull)
                            action { controller.clearTime() }
                        }
                    }
                    menu("Penalty") {
                        item(
                                name = "Add Cone",
                                keyCombination = "Ctrl+]"
                        ) {
                            action { controller.incrementCones(selectedItem!!) }
                            enableWhen(selectionModel.selectedItemProperty().isNotNull)
                        }
                        item(
                                name = "Remove Cone",
                                keyCombination = "Ctrl+["
                        ) {
                            action { controller.decrementCones(selectedItem!!) }
                            enableWhen {
                                selectionModel.selectedItemProperty().isNotNull
                                        .and(selectionModel.selectedItemProperty().select { it.conesProperty }.booleanBinding { (it as Int?) ?: -1 > 0 })
                            }
                        }
                        checkmenuitem(
                                name = "Re-Run",
                                selected = selectionModel.selectedItemProperty().select { it.rerunProperty },
                                keyCombination = "Ctrl+R"
                        ) {
                            action { controller.changeRerun(selectedItem!!, selectedItem!!.rerun) }
                            enableWhen(selectionModel.selectedItemProperty().isNotNull)
                        }
                        checkmenuitem(
                                name = "Did Not Finish",
                                selected = selectionModel.selectedItemProperty().select { it.didNotFinishProperty },
                                keyCombination = "Ctrl+F"
                        ) {
                            action { controller.changeDidNotFinish(selectedItem!!, selectedItem!!.didNotFinish) }
                            enableWhen(selectionModel.selectedItemProperty().isNotNull)
                        }
                        checkmenuitem(
                                name = "Disqualified",
                                selected = selectionModel.selectedItemProperty().select { it.disqualifiedProperty },
                                keyCombination = "Ctrl+Q"
                        ) {
                            action { controller.changeDisqualified(selectedItem!!, selectedItem!!.disqualified) }
                            enableWhen(selectionModel.selectedItemProperty().isNotNull)
                        }
                    }
                    item(
                            name = "Delete",
                            keyCombination = KeyCombination.keyCombination("Ctrl+Delete")
                    ) {
                        enableWhen(selectionModel.selectedItemProperty().isNotNull)
                        action { controller.deleteRun() }
                    }
                }
                overrideFocusTraversal(
                        next = controller.locateAddNextDriverNumbers,
                        previous = controller.locateAddNextDriverNumbers
                )
            }
        }
    }

    val table: TableView<Run>
        get() = root.lookup("#runs-table") as TableView<Run>

    fun selectRunById(selectRunId: UUID) {
        val run = table.items.firstOrNull { it.id == selectRunId } ?: return
        table.selectionModel.select(run)
    }

    private val onTableFocusedListener = ChangeListener<Boolean> { _, _, focused -> controller.onTableFocused(focused) }

    override fun onDock() {
        super.onDock()
        table.focusedProperty().addListener(onTableFocusedListener)
    }

    override fun onUndock() {
        super.onUndock()
        table.focusedProperty().removeListener(onTableFocusedListener)
    }
}
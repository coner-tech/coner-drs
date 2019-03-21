package org.coner.drs.ui.runevent

import javafx.beans.binding.IntegerBinding
import javafx.scene.input.KeyCombination
import javafx.scene.layout.Priority
import javafx.scene.paint.Color
import javafx.scene.text.Text
import org.coner.drs.DrsStylesheet
import org.coner.drs.domain.entity.Run
import tornadofx.*

class RunEventTableView : View() {
    val model: RunEventTableModel by inject()
    val controller: RunEventTableController by inject()

    init {
        controller.init()
    }

    override val root = tableview(model.runsSortedBySequence) {
        isEditable = true
        setSortPolicy { false }
        vgrow = Priority.ALWAYS
        readonlyColumn("Sequence", Run::sequence)
        column("Numbers", Run::registrationNumbersProperty)
        column("Name", Run::registrationDriverNameProperty)
        column("Car Model", Run::registrationCarModelProperty)
        column("Car Color", Run::registrationCarColorProperty)
        column("Time", Run::rawTimeProperty) {
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
                                .and(selectionModel.selectedItemProperty().select { it.conesProperty }.booleanBinding { (it as Int?) ?: -1 > 0})
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
        }
        shortcut("Alt+R") {
            requestFocus()
            var selectIndex = items.indexOfLast { it.rawTime != null }
            if (selectIndex > 0 && selectIndex < items.lastIndex) {
                selectIndex++
            }
            selectionModel.select(selectIndex)
            scrollTo(selectIndex)
        }
        model.runsSortedBySequence.onChange {
            while (it.next()) {
                if (it.wasAdded() && it.addedSize == 1) {
                    scrollTo(it.addedSubList.first())
                }
            }
        }
    }

}
package org.coner.drs

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.transformation.SortedList
import javafx.geometry.Orientation
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import org.coner.drs.util.levenshtein
import org.controlsfx.control.textfield.TextFields
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue
import kotlin.streams.toList

class RunEventFragment : Fragment() {
    val event: Event by param()
    val eventScope = Scope()

    val model: RunEventModel by inject(eventScope)

    init {
        model.event = event
    }

    override val root = titledpane(event.name) {
        isCollapsible = false
        borderpane {
            center { add(find<RunEventTableView>(eventScope)) }
            bottom { add(find<RunEventBottomView>(eventScope)) }
        }
    }
}

class RunEventTableView : View() {
    val model: RunEventModel by inject()
    val controller: RunEventController by inject()

    override val root = tableview(SortedList(model.runs)) {
        isEditable = true
        setSortPolicy { false }
        readonlyColumn("Sequence", Run::sequence)
        column("Category", Run::categoryProperty) {
            makeEditable().useChoiceBox(items = FXCollections.observableArrayList(model.event.categories))
        }
        column("Handicap", Run::handicapProperty) {
            makeEditable().useChoiceBox(items = FXCollections.observableArrayList(model.event.handicaps))
        }
        column("Number", Run::numberProperty) {
            makeEditable()
        }
        column("Time", Run::rawTime) {
            makeEditable()
        }
        column("Cones", Run::conesProperty) {
            makeEditable()
        }
        column("+/-", Run::conesProperty).cellFormat {
            graphic = hbox {
                button("+") {
                    action { controller.incrementCones(this@cellFormat.rowItem) }
                }
                button("-") {
                    action { controller.decrementCones(this@cellFormat.rowItem) }
                    enableWhen { this@cellFormat.rowItem.conesProperty.greaterThan(0) }
                }
            }
        }
        column("Did Not Finish", Run::didNotFinish) {
            makeEditable()
        }
        column("Disqualified", Run::disqualified) {
            makeEditable()
        }
        column("Re-Run", Run::rerun) {
            makeEditable()
        }
        smartResize()
    }

}

class RunEventBottomView : View() {
    private val model: RunEventModel by inject()
    private val controller: RunEventController by inject()
    private val run: RunModel by inject()

    private lateinit var categoryTextField: TextField
    private lateinit var handicapTextField: TextField
    private lateinit var numberTextField: TextField

    init {
        buildNextRun()
    }

    override val root = form {
        fieldset(text = "Next Run", labelPosition = Orientation.VERTICAL) {
            hbox(spacing = 12) {
                hgrow = Priority.NEVER
                field(text = "Sequence", orientation = Orientation.HORIZONTAL) {
                    textfield(run.sequence) {
                        isEditable = false
                        prefColumnCount = 4
                    }
                }
                field(text = "Category", orientation = Orientation.HORIZONTAL) {
                    textfield(run.category) {
                        categoryTextField = this
                        TextFields.bindAutoCompletion(this) {
                            model.categorySuggestions
                                    .filter { it.isNotBlank() }
                                    .filter { it.startsWith(run.category.value) }
                                    .sortedBy { levenshtein(run.category.value, it) }
                        }.apply {
                            setDelay(0)
                        }
                        run.itemProperty.onChange {
                            onNewRun(this)
                        }
                        prefColumnCount = 7
                    }
                }
                field(text = "Handicap", orientation = Orientation.HORIZONTAL) {
                    textfield(run.handicap) {
                        handicapTextField = this
                        required()
                        TextFields.bindAutoCompletion(this) {
                            model.handicapSuggestions
                                    .filter { model.runs.any { someRun ->
                                        someRun.category == run.category.value
                                        && someRun.handicap.startsWith(run.handicap.value)
                                    } }
                                    .sortedBy { levenshtein(run.handicap.value, it) }
                        }.apply {
                            setDelay(0)
                        }
                        prefColumnCount = 5
                    }
                }
                field(text = "Number", orientation = Orientation.HORIZONTAL) {
                    textfield(run.number) {
                        numberTextField = this
                        required()
                        TextFields.bindAutoCompletion(this) {
                            model.numberSuggestions
                                    .filter { model.runs.any { someRun ->
                                        someRun.category == run.category.value
                                        && someRun.handicap == run.handicap.value
                                        && someRun.number.startsWith(run.number.value)
                                    } }
                                    .sortedBy { levenshtein(run.number.value, it) }
                        }.apply {
                            setDelay(0)
                        }
                        prefColumnCount = 5
                    }
                }
                var addButton: Button? = null
                field(text = "Add", orientation = Orientation.HORIZONTAL) {
                    button("Add") {
                        addButton = this
                        enableWhen { run.valid }
                        action { addRun() }
                        tooltip("Shortcut: Ctrl+Enter")
                    }
                    runLater { this.children.first().isVisible = false }
                }
                shortcut("Ctrl+Enter") {
                    addButton?.requestFocus()
                    addRun()
                }
            }
        }
    }

    fun buildNextRun() {
        run.item = Run(event = model.event).apply {
            sequenceProperty.bind(model.runs.sizeProperty.plus(1))
        }
    }

    fun onNewRun(category: TextField) {
        run.validate(decorateErrors = false)
        category.requestFocus()
    }

    fun addRun() {
        val addRun = run.item
        val sequence = addRun.sequence
        addRun.sequenceProperty.unbind()
        addRun.sequence = sequence
        run.commit()
        model.runs.add(addRun)
        buildNextRun()
        controller.buildSuggestions()
    }
}


class RunEventModel : ViewModel() {
    val runs = observableList<Run>()
    val eventProperty = SimpleObjectProperty<Event>()
    var event by eventProperty

    val categorySuggestions = observableList<String>()
    val handicapSuggestions = observableList<String>()
    val numberSuggestions = observableList<String>()
}

class RunEventController : Controller() {
    val model: RunEventModel by inject()

    fun addRun() {
        val run = Run(event = model.event)
        run.sequence = 1 + model.runs.size
        model.runs.add(run)
    }

    fun incrementCones(run: Run) {
        run.cones++
    }

    fun decrementCones(run: Run) {
        run.cones--
    }

    fun buildSuggestions() {
        runAsync {
            model.runs.parallelStream()
                    .map { it.category }
                    .distinct()
                    .sorted()
                    .toList()
        } ui {
            model.categorySuggestions.clear()
            model.categorySuggestions.addAll(it)
        }
        runAsync {
            model.runs.parallelStream()
                    .map { it.handicap }
                    .distinct()
                    .sorted()
                    .toList()
        } ui {
            model.handicapSuggestions.clear()
            model.handicapSuggestions.addAll(it)
        }
        runAsync {
            model.runs.parallelStream()
                    .map { it.number }
                    .distinct()
                    .sorted()
                    .toList()
        } ui {
            model.numberSuggestions.clear()
            model.numberSuggestions.addAll(it)
        }
    }
}

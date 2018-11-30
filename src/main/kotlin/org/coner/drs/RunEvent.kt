package org.coner.drs

import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.disposables.CompositeDisposable
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.transformation.SortedList
import javafx.geometry.Orientation
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import org.coner.drs.db.entityWatchEventConsumer
import org.coner.drs.db.service.RunService
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
    val controller: RunEventController by inject(eventScope)

    init {
        model.event = event
        controller.init()
    }

    override val root = titledpane(event.name) {
        isCollapsible = false
        borderpane {
            center { add(find<RunEventTableView>(eventScope)) }
            bottom { add(find<RunEventBottomView>(eventScope)) }
        }
    }

    override fun onDock() {
        super.onDock()
        controller.docked()
    }

    override fun onUndock() {
        super.onUndock()
        controller.undocked()
    }
}

class RunEventTableView : View() {
    val model: RunEventModel by inject()
    val controller: RunEventController by inject()

    override val root = tableview(SortedList(model.runs, compareBy(Run::sequence))) {
        isEditable = true
        setSortPolicy { false }
        readonlyColumn("Sequence", Run::sequence)
        column("Category", Run::categoryProperty) {
            makeEditable()
        }
        column("Handicap", Run::handicapProperty) {
            makeEditable()
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
        onEditCommit {
            controller.save(it)
        }
    }

}

class RunEventBottomView : View() {
    private val model: RunEventModel by inject()
    private val controller: RunEventController by inject()

    private lateinit var numberTextField: TextField
    private lateinit var categoryTextField: TextField
    private lateinit var handicapTextField: TextField
    private lateinit var addButton: Button

    override val root = form {
        fieldset(text = "Next Run", labelPosition = Orientation.VERTICAL) {
            hbox(spacing = 12) {
                hgrow = Priority.NEVER
                field(text = "Sequence", orientation = Orientation.HORIZONTAL) {
                    textfield(model.nextRun.sequence) {
                        isEditable = false
                        prefColumnCount = 4
                    }
                }
                field(text = "Number", orientation = Orientation.HORIZONTAL) {
                    textfield(model.nextRun.number) {
                        numberTextField = this
                        required()
                        TextFields.bindAutoCompletion(this) {
                            controller.buildNumberHints()
                        }.apply {
                            setDelay(0)
                            setOnAutoCompleted {
                                val singleMatch = controller.onNextRunNumberAutoCompleted()
                                if (singleMatch != null) {
                                    controller.autoCompleteNextRun(singleMatch)
                                }
                                categoryTextField.requestFocus()
                            }
                        }
                        prefColumnCount = 5
                        model.nextRun.itemProperty.onChange {
                            onNewRun(this)
                        }
                    }
                }
                field(text = "Category", orientation = Orientation.HORIZONTAL) {
                    textfield(model.nextRun.category) {
                        categoryTextField = this
                        TextFields.bindAutoCompletion(this) {
                            controller.buildCategoryHints()
                        }.apply {
                            setDelay(0)
                            setOnAutoCompleted {
                                val singleMatch = controller.onNextRunCategoryAutoCompleted()
                                if (singleMatch != null) {
                                    controller.autoCompleteNextRun(singleMatch)
                                }
                                handicapTextField.requestFocus()
                            }
                        }

                        prefColumnCount = 7
                    }
                }
                field(text = "Handicap", orientation = Orientation.HORIZONTAL) {
                    textfield(model.nextRun.handicap) {
                        handicapTextField = this
                        required()
                        TextFields.bindAutoCompletion(this) {
                            controller.buildHandicapHints()
                        }.apply {
                            setDelay(0)
                            setOnAutoCompleted {
                                val singleMatch = controller.onNextRunHandicapAutoCompleted()
                                if (singleMatch != null) {
                                    controller.autoCompleteNextRun(singleMatch)
                                }
                                addButton.requestFocus()
                            }
                        }
                        prefColumnCount = 5
                    }
                }
                field(text = "Add", orientation = Orientation.HORIZONTAL) {
                    button("Add") {
                        addButton = this
                        enableWhen { model.nextRun.valid }
                        action { controller.addNextRun() }
                        tooltip("Shortcut: Ctrl+Enter")
                    }
                    runLater { this.children.first().isVisible = false }
                }
                shortcut("Ctrl+Enter") {
                    if (model.nextRun.isValid) {
                        addButton.requestFocus()
                        controller.addNextRun()
                    }
                }
            }
        }
    }

    fun onNewRun(toFocus: TextField) {
        model.nextRun.validate(decorateErrors = false)
        toFocus.requestFocus()
    }
}


class RunEventModel : ViewModel() {
    val runs = observableList<Run>()
    val nextRun: RunModel by inject()
    val eventProperty = SimpleObjectProperty<Event>()
    var event by eventProperty
    val registrationHints = FXCollections.observableSet<RegistrationHint>()
    val disposables = CompositeDisposable()
}

data class RegistrationHint(val category: String, val handicap: String, val number: String)

class RunEventController : Controller() {
    val model: RunEventModel by inject()
    val service: RunService by inject()

    fun init() {
        service.io.createDrsDbRunsPath(model.event)
        model.runs.onChange { buildRegistrationHints() }
        loadRuns()
        buildNextRun()
    }

    fun loadRuns() {
        runAsync {
            service.list(model.event)
        } success {
            model.runs.clear()
            model.runs.addAll(it)
        }
    }

    fun buildNextRun() {
        model.nextRun.item = Run(event = model.event).apply {
            sequenceProperty.bind(model.runs.sizeProperty.plus(1))
        }
    }

    fun addNextRun() {
        val addRun = model.nextRun.item
        val sequence = addRun.sequence
        addRun.sequenceProperty.unbind()
        addRun.sequence = sequence
        model.nextRun.commit()
        runAsync { service.save(addRun) }
        buildNextRun()
    }

    fun save(run: Run) {
        runAsync { service.save(run) }
    }

    fun incrementCones(run: Run) {
        run.cones++
        runAsync { save(run) }
    }

    fun decrementCones(run: Run) {
        run.cones--
        save(run)
    }

    fun buildRegistrationHints() {
        runAsync {
            model.runs.parallelStream()
                    .map { RegistrationHint(
                            category = it.category,
                            handicap = it.handicap,
                            number = it.number
                    ) }
                    .toList()
        } ui {
            model.registrationHints.clear()
            model.registrationHints.addAll(it)
        }
    }

    fun buildNumberHints(): List<String> {
        if (model.nextRun.number.value.isBlank()) return emptyList()
        var stream = model.registrationHints.parallelStream()
                .filter { it.number.startsWith(model.nextRun.number.value) }
        if (model.nextRun.category.value.isNotBlank()) {
            stream = stream.filter { it.category == model.nextRun.category.value }
        }
        if (model.nextRun.handicap.value.isNotBlank()) {
            stream = stream.filter { it.handicap == model.nextRun.handicap.value }
        }
        return stream.map { it.number }
                .distinct()
                .toList()
                .sortedBy { levenshtein(it, model.nextRun.number.value) }
    }

    fun buildCategoryHints(): List<String> {
        if (model.nextRun.category.value.isBlank()) return emptyList()
        var stream = model.registrationHints.parallelStream()
                .filter { it.category.startsWith(model.nextRun.category.value) }
        if (model.nextRun.number.value.isNotBlank()) {
            stream = stream.filter { it.number == model.nextRun.number.value }
        }
        if (model.nextRun.handicap.value.isNotBlank()) {
            stream = stream.filter { it.handicap == model.nextRun.handicap.value }
        }
        return stream.map { it.category }
                .distinct()
                .toList()
                .sortedBy { levenshtein(it, model.nextRun.category.value) }
    }

    fun buildHandicapHints(): List<String> {
        if (model.nextRun.handicap.value.isBlank()) return emptyList()
        var stream = model.registrationHints.parallelStream()
                .filter { it.handicap.startsWith(model.nextRun.handicap.value) }
        if (model.nextRun.number.value.isNotBlank()) {
            stream = stream.filter { it.number == model.nextRun.number.value }
        }
        if (model.nextRun.category.value.isNotBlank()) {
            stream = stream.filter { it.category == model.nextRun.category.value }
        }
        val hints = stream.map { it.handicap }
                .distinct()
                .toList()
                .sortedBy { levenshtein(it, model.nextRun.handicap.value) }
        return hints
    }

    fun onNextRunNumberAutoCompleted(): RegistrationHint? {
        return model.registrationHints.singleOrNull { it.number == model.nextRun.number.value }
    }

    fun onNextRunCategoryAutoCompleted(): RegistrationHint? {
        return model.registrationHints.singleOrNull { it.category == model.nextRun.category.value }
    }

    fun onNextRunHandicapAutoCompleted(): RegistrationHint? {
        return model.registrationHints.singleOrNull { it.handicap == model.nextRun.handicap.value }

    }

    fun autoCompleteNextRun(singleMatch: RegistrationHint) {
        model.nextRun.number.value = singleMatch.number
        model.nextRun.category.value = singleMatch.category
        model.nextRun.handicap.value = singleMatch.handicap
    }

    fun docked() {
        model.disposables.add(service.watchList(model.event)
                .observeOnFx()
                .subscribe(entityWatchEventConsumer(
                        idProperty = Run::id,
                        list = model.runs
                ))
        )
    }

    fun undocked() {
        model.disposables.clear()
    }
}

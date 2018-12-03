package org.coner.drs

import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.disposables.CompositeDisposable
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.transformation.SortedList
import javafx.geometry.Orientation
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.stage.Modality
import org.coner.drs.io.db.entityWatchEventConsumer
import org.coner.drs.io.db.service.RunService
import org.coner.drs.io.timer.TimerService
import org.coner.drs.util.levenshtein
import org.coner.timer.model.FinishTriggerElapsedTimeOnly
import org.coner.timer.output.TimerOutputWriter
import org.controlsfx.control.textfield.TextFields
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue
import java.io.File
import java.io.FileNotFoundException
import kotlin.streams.toList

class RunEventFragment : Fragment("Run Event") {
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
        prefHeightProperty().bind(parentProperty().select { (it as Region).heightProperty() })
        borderpane {
            center {
                vgrow = Priority.ALWAYS
                add(find<RunEventTableView>(eventScope))
            }
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
        vgrow = Priority.ALWAYS
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
    override val root = hbox {
        add(find<RunEventAddNextRunView>())
        pane {
            hgrow = Priority.ALWAYS
        }
        add(find<RunEventTimerView>())
    }
}

class RunEventAddNextRunView : View() {
    private val model: RunEventModel by inject()
    private val controller: RunEventController by inject()

    private lateinit var numberTextField: TextField
    private lateinit var categoryTextField: TextField
    private lateinit var handicapTextField: TextField
    private lateinit var addButton: Button

    override val root = form {
        fieldset(text = "Next Driver", labelPosition = Orientation.VERTICAL) {
            hbox(spacing = 12) {
                hgrow = Priority.NEVER
                field(text = "Sequence", orientation = Orientation.HORIZONTAL) {
                    textfield(model.nextDriver.sequence) {
                        isEditable = false
                        prefColumnCount = 4
                    }
                }
                field(text = "Number", orientation = Orientation.HORIZONTAL) {
                    textfield(model.nextDriver.number) {
                        numberTextField = this
                        required()
                        TextFields.bindAutoCompletion(this) {
                            controller.buildNumberHints()
                        }.apply {
                            setDelay(0)
                            setOnAutoCompleted {
                                val singleMatch = controller.onNextDriverNumberAutoCompleted()
                                if (singleMatch != null) {
                                    controller.autoCompleteNextDriver(singleMatch)
                                }
                                categoryTextField.requestFocus()
                            }
                        }
                        prefColumnCount = 5
                        model.nextDriver.itemProperty.onChange {
                            onNewRun(this)
                        }
                    }
                }
                field(text = "Category", orientation = Orientation.HORIZONTAL) {
                    textfield(model.nextDriver.category) {
                        categoryTextField = this
                        TextFields.bindAutoCompletion(this) {
                            controller.buildCategoryHints()
                        }.apply {
                            setDelay(0)
                            setOnAutoCompleted {
                                val singleMatch = controller.onNextDriverCategoryAutoCompleted()
                                if (singleMatch != null) {
                                    controller.autoCompleteNextDriver(singleMatch)
                                }
                                handicapTextField.requestFocus()
                            }
                        }

                        prefColumnCount = 7
                    }
                }
                field(text = "Handicap", orientation = Orientation.HORIZONTAL) {
                    textfield(model.nextDriver.handicap) {
                        handicapTextField = this
                        required()
                        TextFields.bindAutoCompletion(this) {
                            controller.buildHandicapHints()
                        }.apply {
                            setDelay(0)
                            setOnAutoCompleted {
                                val singleMatch = controller.onNextDriverHandicapAutoCompleted()
                                if (singleMatch != null) {
                                    controller.autoCompleteNextDriver(singleMatch)
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
                        enableWhen { model.nextDriver.valid }
                        action { controller.addNextDriver() }
                        tooltip("Shortcut: Ctrl+Enter")
                    }
                    runLater { this.children.first().isVisible = false }
                }
                shortcut("Ctrl+Enter") {
                    if (model.nextDriver.isValid) {
                        addButton.requestFocus()
                        controller.addNextDriver()
                    }
                }
            }
        }
    }

    fun onNewRun(toFocus: TextField) {
        model.nextDriver.validate(decorateErrors = false)
        toFocus.requestFocus()
    }

    override fun onDock() {
        super.onDock()
        controller.buildNextDriver()
    }
}

class RunEventTimerView : View() {
    val model: RunEventModel by inject()
    val controller: RunEventController by inject()
    val timerService: TimerService by inject()
    override val root = form {
        alignment = Pos.CENTER_RIGHT
        fieldset(text = "Timer", labelPosition = Orientation.VERTICAL) {
            hbox(spacing = 12) {
                hgrow = Priority.NEVER
                field("Operation") {
                    button(model.controlTextProperty) {
                        enableWhen { model.timerConfigurationProperty.isNotNull }
                        action { runAsyncWithProgress { controller.toggleTimer() } }
                    }
                }
                field(text = "Configuration") {
                    button("Configure") {
                        enableWhen { timerService.model.timerProperty.isNull }
                        action { showConfiguration() }
                    }
                    text(model.timerConfigurationTextProperty)
                }
            }
        }
    }

    private fun showConfiguration() {
        find<RunEventTimerConfigurationView>().openModal(
                modality = Modality.APPLICATION_MODAL,
                owner = currentWindow,
                block = true,
                escapeClosesWindow = true
        )
    }

    override fun onDock() {
        super.onDock()
        model.controlTextProperty.bind(timerService.model.timerProperty.stringBinding {
            if (it == null) "Start" else "Stop"
        })
        model.timerConfigurationTextProperty.bind(model.timerConfigurationProperty.stringBinding {
            when (it) {
                null -> "Not configured"
                is TimerConfiguration.FileInput -> "File: ${it.file.name}"
                is TimerConfiguration.SerialPortInput -> "Serial port: ${it.port}"
            }
        })
    }

    override fun onUndock() {
        super.onUndock()
        model.controlTextProperty.unbind()
        model.timerConfigurationTextProperty.unbind()
    }
}

class RunEventTimerConfigurationView : View("Configure Timer") {

    private val model: RunEventTimerConfigurationModel by inject()
    private val runEventModel: RunEventModel by inject()
    private val timerService: TimerService by inject()

    override val root = form {
        minWidth = 640.0
        minHeight = 480.0
        fieldset(title) {
            field("Type") {
                choicebox(property = model.typeProperty, values = model.types)
            }
            field("Port") {
                combobox(property = model.serialPortProperty, values = model.serialPorts).required()
                button("Refresh") { action { refreshSerialPorts() }  }
                managedWhen { model.typeProperty.isEqualTo(TimerConfiguration.SerialPortInput.label) }
                visibleWhen(managedProperty())
            }
            field("File") {
                textfield(model.inputFileProperty) {
                    required()
                }
                button("Choose") { action { chooseInputFile() } }
                managedWhen { model.typeProperty.isEqualTo(TimerConfiguration.FileInput.label) }
                visibleWhen(managedProperty())
            }
        }
        buttonbar {
            button("Clear", ButtonBar.ButtonData.OTHER) {
                action { clearConfiguration() }
            }
            button("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE) {
                action { close() }
            }
            button("Apply", ButtonBar.ButtonData.OK_DONE) {
                enableWhen {
                    booleanBinding(model.typeProperty, model.serialPortProperty, model.inputFileProperty) {
                        when (model.type) {
                            TimerConfiguration.SerialPortInput.label -> model.serialPort?.isNotBlank() ?: false
                            TimerConfiguration.FileInput.label -> model.inputFile?.isNotBlank() ?: false
                            else -> throw IllegalArgumentException("Unrecognized type: ${model.type}")
                        }
                    }
                }
                action { save() }
            }
        }
    }

    override fun onDock() {
        super.onDock()
        refreshSerialPorts()
    }

    override fun onUndock() {
        super.onUndock()
        model.rollback()
    }

    private fun refreshSerialPorts() {
        runAsync {
            timerService.listSerialPorts()
        } ui {
            model.serialPorts.clear()
            model.serialPorts.addAll(it)
        }
    }

    private fun chooseInputFile() {
        val choice = chooseFile(
                title = "Choose Timer Input File",
                mode = FileChooserMode.Single,
                owner = this@RunEventTimerConfigurationView.currentWindow,
                filters = emptyArray()
        ).firstOrNull()
        model.inputFile = choice?.absolutePath
    }

    private fun save() {
        runEventModel.timerConfiguration = when (model.type) {
            TimerConfiguration.SerialPortInput.label -> {
                TimerConfiguration.SerialPortInput(model.serialPort)
            }
            TimerConfiguration.FileInput.label -> {
                val file = File(model.inputFile)
                if (!file.exists() || !file.isFile) throw FileNotFoundException("File not found: ${model.inputFile}")
                TimerConfiguration.FileInput(file)
            }
            else -> throw IllegalArgumentException("Unrecognized type: ${model.type}")
        }
        close()
    }

    private fun clearConfiguration() {
        runEventModel.timerConfiguration = null
        close()
    }
}

class RunEventTimerConfigurationModel : ViewModel() {
    val types = observableList(
            TimerConfiguration.SerialPortInput.label,
            TimerConfiguration.FileInput.label
    )

    val typeProperty = SimpleStringProperty(this, "type", TimerConfiguration.SerialPortInput.label)
    var type by typeProperty

    val serialPortProperty = SimpleStringProperty(this, "serialPort")
    var serialPort by serialPortProperty

    val inputFileProperty = SimpleStringProperty(this, "inputFile")
    var inputFile by inputFileProperty


    val serialPorts = observableList<String>()

}

private val TimerConfiguration.SerialPortInput.Companion.label: String get() = "Serial Port"
private val TimerConfiguration.FileInput.Companion.label: String get() = "File"

class RunEventModel : ViewModel() {
    val runs = observableList<Run>()
    val nextDriver: NextDriverModel by inject()
    val eventProperty = SimpleObjectProperty<Event>()
    var event by eventProperty
    val registrationHints = FXCollections.observableSet<RegistrationHint>()
    val disposables = CompositeDisposable()

    val controlTextProperty = SimpleStringProperty(this, "controlText")
    var controlText by controlTextProperty

    val timerConfigurationProperty = SimpleObjectProperty<TimerConfiguration>(this, "timerConfiguration", null)
    var timerConfiguration by timerConfigurationProperty

    val timerConfigurationTextProperty = SimpleStringProperty(this, "timerConfigurationText", null)
    var timerConfigurationText by timerConfigurationTextProperty

}

data class RegistrationHint(val category: String, val handicap: String, val number: String)

class RunEventController : Controller() {
    val model: RunEventModel by inject()
    val runService: RunService by inject()
    val timerService: TimerService by inject()

    fun init() {
        runService.io.createDrsDbRunsPath(model.event)
        model.runs.onChange { buildRegistrationHints() }
        loadRuns()
    }

    fun loadRuns() {
        runAsync {
            runService.list(model.event)
        } success {
            model.runs.clear()
            model.runs.addAll(it)
        }
    }

    fun buildNextDriver() {
        model.nextDriver.item = Run(event = model.event).apply {
            sequenceProperty.bind(integerBinding(model.runs) {
                val runs = synchronized(model.runs) { model.runs.toList() }
                val firstRunWithoutDriver = runs.parallelStream()
                        .filter { it.category.isBlank() && it.handicap.isBlank() && it.number.isBlank() }
                        .sorted(compareBy(Run::sequence))
                        .findFirst().orElse(null)
                if (firstRunWithoutDriver == null) {
                    runs.size + 1
                } else {
                    firstRunWithoutDriver.sequence
                }
            })
        }
    }

    fun addNextDriver() {
        val addRun = model.nextDriver.item
        val sequence = addRun.sequence
        addRun.sequenceProperty.unbind()
        addRun.sequence = sequence
        model.nextDriver.commit()
        runAsync { runService.insertNextDriver(addRun) }
        buildNextDriver()
    }

    fun save(run: Run) {
        runAsync { runService.save(run) }
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
        val runs = synchronized(model.runs) { model.runs.toList() }
        runAsync {
            runs.parallelStream()
                    .map { RegistrationHint(
                            category = it.category,
                            handicap = it.handicap,
                            number = it.number
                    ) }
                    .distinct()
                    .toList()
        } ui {
            model.registrationHints.clear()
            model.registrationHints.addAll(it)
        }
    }

    fun buildNumberHints(): List<String> {
        if (model.nextDriver.number.value.isBlank()) return emptyList()
        val registrationHints = synchronized(model.registrationHints) { model.registrationHints.toList() }
        var stream = registrationHints.parallelStream()
                .filter { it.number.startsWith(model.nextDriver.number.value) }
        if (model.nextDriver.category.value.isNotBlank()) {
            stream = stream.filter { it.category == model.nextDriver.category.value }
        }
        if (model.nextDriver.handicap.value.isNotBlank()) {
            stream = stream.filter { it.handicap == model.nextDriver.handicap.value }
        }
        return stream.map { it.number }
                .distinct()
                .toList()
                .sortedBy { levenshtein(it, model.nextDriver.number.value) }
    }

    fun buildCategoryHints(): List<String> {
        if (model.nextDriver.category.value.isBlank()) return emptyList()
        val registrationHints = synchronized(model.registrationHints) { model.registrationHints.toList() }
        var stream = registrationHints.parallelStream()
                .filter { it.category.startsWith(model.nextDriver.category.value) }
        if (model.nextDriver.number.value.isNotBlank()) {
            stream = stream.filter { it.number == model.nextDriver.number.value }
        }
        if (model.nextDriver.handicap.value.isNotBlank()) {
            stream = stream.filter { it.handicap == model.nextDriver.handicap.value }
        }
        return stream.map { it.category }
                .distinct()
                .toList()
                .sortedBy { levenshtein(it, model.nextDriver.category.value) }
    }

    fun buildHandicapHints(): List<String> {
        if (model.nextDriver.handicap.value.isBlank()) return emptyList()
        val registrationHints = synchronized(model.registrationHints) { model.registrationHints.toList() }
        var stream = registrationHints.parallelStream()
                .filter { it.handicap.startsWith(model.nextDriver.handicap.value) }
        if (model.nextDriver.number.value.isNotBlank()) {
            stream = stream.filter { it.number == model.nextDriver.number.value }
        }
        if (model.nextDriver.category.value.isNotBlank()) {
            stream = stream.filter { it.category == model.nextDriver.category.value }
        }
        val hints = stream.map { it.handicap }
                .distinct()
                .toList()
                .sortedBy { levenshtein(it, model.nextDriver.handicap.value) }
        return hints
    }

    fun onNextDriverNumberAutoCompleted(): RegistrationHint? {
        return model.registrationHints.singleOrNull { it.number == model.nextDriver.number.value }
    }

    fun onNextDriverCategoryAutoCompleted(): RegistrationHint? {
        return model.registrationHints.singleOrNull { it.category == model.nextDriver.category.value }
    }

    fun onNextDriverHandicapAutoCompleted(): RegistrationHint? {
        return model.registrationHints.singleOrNull { it.handicap == model.nextDriver.handicap.value }

    }

    fun autoCompleteNextDriver(singleMatch: RegistrationHint) {
        model.nextDriver.number.value = singleMatch.number
        model.nextDriver.category.value = singleMatch.category
        model.nextDriver.handicap.value = singleMatch.handicap
    }

    fun docked() {
        model.disposables.add(runService.watchList(model.event)
                .observeOnFx()
                .subscribe(entityWatchEventConsumer(
                        idProperty = Run::id,
                        list = model.runs
                ))
        )
    }

    fun undocked() {
        model.disposables.clear()
        timerService.stop()
    }

    fun toggleTimer() {
        if (timerService.model.timer == null) {
            val config = model.timerConfiguration
            when (config) {
                is TimerConfiguration.SerialPortInput -> {
                    timerService.startCommPortTimer(config.port, timerOutputWriter)
                }
                is TimerConfiguration.FileInput -> {
                    timerService.startFileInputTimer(config.file, timerOutputWriter)
                }
            }
        } else {
            timerService.stop()
        }
    }

    private val timerOutputWriter = object : TimerOutputWriter<FinishTriggerElapsedTimeOnly> {
        override fun write(input: FinishTriggerElapsedTimeOnly) {
            runService.addTimeToFirstRunInSequenceWithoutRawTime(model.event, input.et)
        }
    }
}

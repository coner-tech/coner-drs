package org.coner.drs

import com.github.thomasnield.rxkotlinfx.observeOnFx
import io.reactivex.disposables.CompositeDisposable
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.transformation.SortedList
import javafx.geometry.Orientation
import javafx.geometry.Side
import javafx.scene.control.Button
import javafx.scene.control.ButtonBar
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import javafx.scene.layout.Region
import javafx.stage.Modality
import javafx.util.StringConverter
import org.coner.drs.io.db.entityWatchEventConsumer
import org.coner.drs.io.db.service.RunService
import org.coner.drs.io.timer.TimerService
import org.coner.drs.util.bindAutoCompletion
import org.coner.drs.util.levenshtein
import org.coner.timer.model.FinishTriggerElapsedTimeOnly
import org.coner.timer.output.TimerOutputWriter
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
            left { add(find<RunEventLeftDrawerView>(eventScope)) }
            right { add(find<RunEventRightDrawerView>(eventScope)) }
            center {
                vgrow = Priority.ALWAYS
                add(find<RunEventTableView>(eventScope))
            }
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

class RunEventLeftDrawerView : View() {
    override val root = drawer(side = Side.LEFT) {
        item<RunEventAddNextDriverView>(expanded = true)
    }
}

class RunEventRightDrawerView : View() {
    override val root = drawer(side = Side.RIGHT) {
        item<RunEventTimerView>(showHeader = true)
    }
}

class RunEventAddNextDriverView : View("Add Next Driver") {
    private val model: RunEventAddNextDriverModel by inject()
    private val controller: RunEventAddNextDriverController by inject()

    private lateinit var numberTextField: TextField
    private lateinit var categoryTextField: TextField
    private lateinit var handicapTextField: TextField
    private lateinit var addButton: Button

    override val root = form {
        fieldset(text = title, labelPosition = Orientation.VERTICAL) {
            hgrow = Priority.NEVER
            field(text = "Sequence") {
                textfield(model.nextDriver.sequence) {
                    isEditable = false
                    prefColumnCount = 4
                }
            }
            field(text = "Numbers") {
                textfield(model.numbersFieldProperty) {
                    numberTextField = this
                    required()
                    bindAutoCompletion(suggestionsProvider = { controller.buildNumbersHints() }) {
                        setDelay(0)
                    }
                    prefColumnCount = 5
                    model.nextDriver.itemProperty.onChange {
                        onNewRun(this)
                    }
                    promptTextProperty().bind(model.driverAutoCompleteOrderPreferenceProperty.stringBinding { it?.text })
                }
            }
            buttonbar {
                button("Add") {
                    addButton = this
                    enableWhen { model.nextDriver.valid }
                    action { controller.addNextDriver() }
                    tooltip("Shortcut: Ctrl+Enter")
                    isDefaultButton = true
                }
            }
            shortcut("Ctrl+Enter") {
                if (model.nextDriver.isValid) {
                    controller.addNextDriver()
                }
            }
        }
        pane {
            vgrow = Priority.ALWAYS
        }
        fieldset(text = "Preferences" ,labelPosition = Orientation.VERTICAL) {
            field("Driver Auto-Complete Order") {
                choicebox(
                        property = model.driverAutoCompleteOrderPreferenceProperty,
                        values = model.driverAutoCompleteOrderPreferences
                ) {
                    converter = DriverAutoCompleteOrderPreferenceStringConverter()
                }
            }
        }
    }

    private inner class DriverAutoCompleteOrderPreferenceStringConverter(
    ) : StringConverter<DriverAutoCompleteOrderPreference>() {
        override fun toString(p0: DriverAutoCompleteOrderPreference) = p0.text

        override fun fromString(p0: String) = model.driverAutoCompleteOrderPreferences.first { it.text == p0 }

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

class RunEventAddNextDriverModel : ViewModel() {

    val nextDriver: NextDriverModel by inject()
    val registrationHints = FXCollections.observableSet<RegistrationHint>()

    val numbersFieldProperty = SimpleStringProperty(this, "numbers", "")
    var numbersField by numbersFieldProperty

    val driverAutoCompleteOrderPreferenceProperty = SimpleObjectProperty<DriverAutoCompleteOrderPreference>(
            this,
            "driverAutoCompleteOrderPreference",
            DriverAutoCompleteOrderPreference.NumberCategoryHandicap
    )
    var driverAutoCompleteOrderPreference by driverAutoCompleteOrderPreferenceProperty

    val driverAutoCompleteOrderPreferences = listOf(
            DriverAutoCompleteOrderPreference.NumberCategoryHandicap,
            DriverAutoCompleteOrderPreference.CategoryHandicapNumber
    )
}

sealed class DriverAutoCompleteOrderPreference {
    abstract val text: String
    abstract val stringConverter: StringConverter<RegistrationHint>

    object NumberCategoryHandicap : DriverAutoCompleteOrderPreference() {
        override val text = "Number Category Handicap"
        override val stringConverter = object : StringConverter<RegistrationHint>() {
            override fun toString(p0: RegistrationHint) = arrayOf(
                    p0.number,
                    p0.category,
                    p0.handicap
            ).filterNot { it.isBlank() }.joinToString(" ")

            override fun fromString(p0: String): RegistrationHint {
                val split = p0.split(" ")
                return when(split.size) {
                    2 -> RegistrationHint(number = split[0], category = "", handicap = split[1])
                    3 -> RegistrationHint(number = split[0], category = split[1], handicap = split[2])
                    else -> throw IllegalArgumentException("Invalid registration hint: $p0")
                }
            }
        }
    }

    object CategoryHandicapNumber : DriverAutoCompleteOrderPreference() {
        override val text = "Category Handicap Number"
        override val stringConverter = object : StringConverter<RegistrationHint>() {
            override fun toString(p0: RegistrationHint) = arrayOf(
                    p0.category,
                    p0.handicap,
                    p0.number
            ).filterNot { it.isBlank() }.joinToString(" ")

            override fun fromString(p0: String): RegistrationHint {
                val split = p0.split(" ")
                return when (split.size) {
                    2 -> RegistrationHint(category = "", handicap = split[0], number = split[1])
                    3 -> RegistrationHint(category = split[0], handicap = split[1], number = split[2])
                    else -> throw IllegalArgumentException("Invalid registration hint: $p0")
                }
            }
        }
    }
}

class RunEventAddNextDriverController : Controller() {

    val model: RunEventAddNextDriverModel by inject()
    val runEventModel: RunEventModel by inject()
    val runService: RunService by inject()

    init {
        runEventModel.runs.onChange { buildRegistrationHints() }
        model.driverAutoCompleteOrderPreferenceProperty.addListener { observable, old, new ->
            reformatNumbersField(old, new)
        }
    }

    fun buildNextDriver() {
        model.nextDriver.item = Run(event = runEventModel.event).apply {
            sequenceProperty.bind(integerBinding(runEventModel.runs) {
                val runs = kotlin.synchronized(runEventModel.runs) { runEventModel.runs.toList() }
                val firstRunWithoutDriver = runs.parallelStream()
                        .filter { it.category.isBlank() && it.handicap.isBlank() && it.number.isBlank() }
                        .sorted(kotlin.comparisons.compareBy(org.coner.drs.Run::sequence))
                        .findFirst().orElse(null)
                if (firstRunWithoutDriver == null) {
                    runs.size + 1
                } else {
                    firstRunWithoutDriver.sequence
                }
            })
        }
        model.numbersField = ""
        model.validate(decorateErrors = false)
    }

    fun addNextDriver() {
        val addRun = model.nextDriver.item.let {
            val sequence = it.sequence
            it.sequenceProperty.unbind()
            it.sequence = sequence
            val nextDriverNumbers = model.driverAutoCompleteOrderPreference.stringConverter.fromString(model.numbersField)
            it.number = nextDriverNumbers.number
            it.category = nextDriverNumbers.category
            it.handicap = nextDriverNumbers.handicap
            it
        }
        model.nextDriver.commit()
        runAsync { runService.insertNextDriver(addRun) }
        buildNextDriver()
    }

    fun buildRegistrationHints() {
        val runs = synchronized(runEventModel.runs) { runEventModel.runs.toList() }
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

    fun buildNumbersHints(): List<String> {
        if (model.numbersField.isBlank()) return emptyList()
        val registrationHints = synchronized(model.registrationHints) { model.registrationHints.toList() }
        val converter = model.driverAutoCompleteOrderPreference.stringConverter
        return registrationHints.parallelStream()
                .map { converter.toString(it) }
                .filter { it.startsWith(model.numbersField) }
                .sorted { left, right -> levenshtein(left, right) }
                .toList()
    }

    fun reformatNumbersField(old: DriverAutoCompleteOrderPreference, new: DriverAutoCompleteOrderPreference) {
        val numbers = model.numbersField
        model.numbersField = try {
            if (numbers?.isNotBlank() == true) {
                val registrationHint = old.stringConverter.fromString(numbers)
                new.stringConverter.toString(registrationHint)
            } else ""
        } catch (t: Throwable) {
            ""
        }
    }

}

class RunEventTimerView : View("Timer") {
    val model: RunEventModel by inject()
    val controller: RunEventController by inject()
    val timerService: TimerService by inject()
    override val root = form {
        fieldset(labelPosition = Orientation.VERTICAL) {
            field("Operation") {
                button(model.controlTextProperty) {
                    enableWhen { model.timerConfigurationProperty.isNotNull }
                    action { runAsyncWithProgress { controller.toggleTimer() } }
                }
            }
            field(text = "Configuration") {
                vbox(spacing = 8) {
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
    val eventProperty = SimpleObjectProperty<Event>()
    var event by eventProperty
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

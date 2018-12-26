package org.coner.drs.ui.run_event

import org.coner.drs.Run
import org.coner.drs.io.service.RunService
import org.coner.drs.util.levenshtein
import tornadofx.*
import kotlin.streams.toList

class AddNextDriverController : Controller() {

    val model: AddNextDriverModel by inject()
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
                val runs = synchronized(runEventModel.runs) { runEventModel.runs.toList() }
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
                    .map {
                        RegistrationHint(
                                category = it.category,
                                handicap = it.handicap,
                                number = it.number
                        )
                    }
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
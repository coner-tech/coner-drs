package org.coner.drs.ui.runevent

import com.github.thomasnield.rxkotlinfx.observeOnFx
import com.github.thomasnield.rxkotlinfx.onChangedObservable
import org.coner.drs.domain.entity.DriverAutoCompleteOrderPreference
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RegistrationHint
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.io.service.RunIoService
import tornadofx.*
import java.util.concurrent.TimeUnit

class AddNextDriverController : Controller() {

    val model: AddNextDriverModel by inject()
    val runEventModel: RunEventModel by inject()
    val runIoService: RunIoService by inject()
    val registrationService: RegistrationService by inject()

    init {
        runEventModel.registrations.onChange { buildRegistrationHints() }
        model.registrationHints.onChangedObservable()
                .observeOnFx()
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribe { buildRegistrationHintsToRegistrationsMap() }
        model.driverAutoCompleteOrderPreferenceProperty.addListener { observable, old, new ->
            reformatNumbersField(old, new)
        }
        model.registrationForNumbersProperty.bind(model.numbersFieldProperty.objectBinding(model.numbersFieldProperty) {
            val hint = try {
                model.driverAutoCompleteOrderPreference.stringConverter.fromString(model.numbersField)
            } catch (t: Throwable) {
                null
            } ?: return@objectBinding null
            val registration = runEventModel.registrations.firstOrNull {
                it.number == hint.number
                && it.category == hint.category
                && it.handicap == hint.handicap
            }
            registration
        })
    }

    fun buildNextDriver() {
        model.nextDriver.item = Run(event = runEventModel.event).apply {
            sequenceProperty.bind(integerBinding(runEventModel.runs) {
                val runs = synchronized(runEventModel.runs) { runEventModel.runs.toList() }
                val firstRunWithoutDriver = runs.parallelStream()
                        .filter {
                            it.registrationCategory.isBlank()
                                    && it.registrationHandicap.isBlank()
                                    && it.registrationNumber.isBlank()
                        }
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
            it.registration = model.registrationForNumbers
            it
        }
        model.nextDriver.commit()
        runAsync { runIoService.insertNextDriver(addRun) }
        buildNextDriver()
    }

    fun buildRegistrationHints() {
        runAsync {
            registrationService.buildRegistrationHints(runEventModel.registrations)
        } ui {
            model.registrationHints.clear()
            model.registrationHints.addAll(it)
        }
    }

    fun buildRegistrationHintsToRegistrationsMap() {
        val registrations = synchronized(runEventModel.registrations) { runEventModel.registrations.toList() }
        val registrationHints = synchronized(model.registrationHints) { model.registrationHints.toList() }
        val map = mutableMapOf<RegistrationHint, Registration>()
        runAsync {
            registrationHints.forEach { hint ->
                map[hint] = registrations.single {
                    it.number == hint.number
                            && it.category == hint.category
                            && it.handicap == hint.handicap
                }
            }
            map
        } ui {
            model.registrationHintsToRegistrations.clear()
            model.registrationHintsToRegistrations.putAll(map)
        }
    }

    fun buildNumbersHints(): List<String> {
        return registrationService.buildNumbersFieldHints(
                numbersField = model.numbersField,
                registrationHints = model.registrationHints,
                autoCompleteOrderPreference = model.driverAutoCompleteOrderPreference
        )
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
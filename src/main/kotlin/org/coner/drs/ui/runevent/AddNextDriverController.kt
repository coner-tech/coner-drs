package org.coner.drs.ui.runevent

import com.github.thomasnield.rxkotlinfx.observeOnFx
import com.github.thomasnield.rxkotlinfx.onChangedObservable
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.RegistrationHint
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.service.RegistrationService
import org.coner.drs.io.gateway.RunGateway
import tornadofx.*
import java.util.concurrent.TimeUnit

class AddNextDriverController : Controller() {

    val model: AddNextDriverModel by inject()
    val runEventModel: RunEventModel by inject()
    val runGateway: RunGateway by inject()
    val registrationService: RegistrationService by inject()

    init {
        runEventModel.registrations.onChange { buildRegistrationHints() }
        model.registrationHints.onChangedObservable()
                .observeOnFx()
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribe { buildRegistrationHintsToRegistrationsMap() }
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
            it.registration = null // TODO
            it
        }
        model.nextDriver.commit()
        runAsync { runGateway.insertNextDriver(addRun) }
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
                registrationHints = model.registrationHints
        )
    }

}
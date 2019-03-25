package org.coner.drs.domain.service

import javafx.concurrent.Task
import org.coner.drs.domain.entity.Registration
import org.coner.drs.domain.entity.Run
import org.coner.drs.domain.entity.RunEvent
import org.coner.drs.io.gateway.RunGateway
import tornadofx.*
import java.math.BigDecimal

class RunService : Controller() {

    val gateway: RunGateway by inject()

    fun addNextDriver(event: RunEvent, registration: Registration) {
        val run = event.runForNextDriver
        run.registration = registration
        if (!event.runs.contains(run)) {
            event.runs.add(run)
        }
        event.runForNextDriverBinding.invalidate()
        save(run)
    }

    fun addNextTime(event: RunEvent, time: BigDecimal) {
        val run = event.runForNextTime
        run.rawTime = time
        if (!event.runs.contains(run)) {
            event.runs.add(run)
        }
        event.runForNextTimeBinding.invalidate()
        save(run)
    }

    fun incrementCones(run: Run) {
        run.cones++
        save(run)
    }

    fun decrementCones(run: Run) {
        run.cones--
        save(run)
    }

    fun changeDidNotFinish(run: Run, newValue: Boolean) {
        run.didNotFinish = newValue
        save(run)
    }

    fun changeRerun(run: Run, newValue: Boolean) {
        run.rerun = newValue
        save(run)
    }

    fun changeDisqualified(run: Run, newValue: Boolean) {
        run.disqualified = newValue
        save(run)
    }

    fun changeDriver(run: Run, registration: Registration?) {
        run.registration = registration
        save(run)
    }

    private fun save(run: Run): Task<Run> {
        return runAsync {
            gateway.save(run)
            run
        }
    }
}
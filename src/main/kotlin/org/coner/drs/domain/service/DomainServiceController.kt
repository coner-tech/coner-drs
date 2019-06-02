package org.coner.drs.domain.service

import io.reactivex.Completable
import io.reactivex.Single
import tornadofx.*

class DomainServiceController : Controller() {

    private val model: DomainServiceModel by inject()

    inline fun <reified T> scheduleSingle(noinline run: () -> T): Single<T> {
        val model: DomainServiceModel = find()
        return Single.just(run)
                .flatMap {
                    Single.just(run)
                            .subscribeOn(model.scheduler)
                            .observeOn(model.scheduler)
                            .map { run() }
                }
    }

    fun scheduleCompletable(run: () -> Unit) = Completable.fromAction {
        Completable.fromAction { run() }
                .observeOn(model.scheduler)
                .subscribeOn(model.scheduler)
                .blockingAwait()
    }

    private val onShutdown = Thread {
        println("DomainServiceController.onShutdown")
        model.scheduler.shutdown()
        model.executor.shutdown()
    }

    init {
        Runtime.getRuntime().addShutdownHook(onShutdown)
    }
}
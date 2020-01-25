/*
 * Coner Digital Raw Sheets - reduce the drag of working autocross raw sheets
 * Copyright (C) 2018-2020 Carlton Whitehead
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

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
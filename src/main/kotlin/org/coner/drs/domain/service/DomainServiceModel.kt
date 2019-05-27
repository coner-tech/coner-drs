package org.coner.drs.domain.service

import io.reactivex.schedulers.Schedulers
import tornadofx.*
import java.util.concurrent.Executors

class DomainServiceModel : Component(), ScopedInstance {
    val executor = Executors.newSingleThreadScheduledExecutor()
    val scheduler = Schedulers.from(executor)
}
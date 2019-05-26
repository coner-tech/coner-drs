package org.coner.drs.domain.service

import io.reactivex.schedulers.Schedulers
import tornadofx.*
import java.util.concurrent.Executors

class DomainServiceModel : Component(), ScopedInstance {
    val scheduler = Schedulers.from(Executors.newSingleThreadExecutor())
}
package org.coner.drs.io

import org.coner.snoozle.db.Database
import tornadofx.*
import java.io.File

class DrsIoModel : ViewModel() {
    var db: Database? = null
    var pathToDrsDatabase: File? = null
    var pathToCrispyFishDatabase: File? = null
    val open: Boolean get() = db != null
}
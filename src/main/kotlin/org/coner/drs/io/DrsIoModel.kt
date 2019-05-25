package org.coner.drs.io

import org.coner.snoozle.db.Database
import tornadofx.*
import java.io.File
import java.nio.file.Path

class DrsIoModel : ViewModel() {
    var db: Database? = null
    var pathToDrsDatabase: Path? = null
    var pathToCrispyFishDatabase: File? = null
    val open: Boolean get() = db != null
}
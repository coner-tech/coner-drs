package org.coner.drs.ui.runevent

import javafx.beans.property.SimpleListProperty
import org.coner.drs.domain.entity.Run
import tornadofx.*

class RunEventTableModel : ViewModel() {

    val runsSortedBySequenceProperty = SimpleListProperty<Run>(this, "runsSortedBySequence")
    var runsSortedBySequence by runsSortedBySequenceProperty
}
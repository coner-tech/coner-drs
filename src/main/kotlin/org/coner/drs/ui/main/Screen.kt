package org.coner.drs.ui.main

import org.coner.drs.domain.entity.Event
import java.io.File
import java.nio.file.Path

sealed class Screen {
    object Start : Screen()
    class ChooseEvent(val pathToDrsDb: Path, val pathToCfDb: File) : Screen()
    class RunEvent(val event: Event) : Screen()
}
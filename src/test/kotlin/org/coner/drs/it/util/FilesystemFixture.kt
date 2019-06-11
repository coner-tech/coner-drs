package org.coner.drs.it.util

import java.nio.file.Files
import java.nio.file.Path

class FilesystemFixture(
        val root: Path
) {
    val appConfigBasePath: Path = root.resolve("appConfigBasePath").apply { Files.createDirectory(this) }
    val rawSheetDatabase: Path = root.resolve("rawSheetDatabase").apply { Files.createDirectory(this) }
    val crispyFishDatabase: Path = root.resolve("crispyFishDatabase").apply { Files.createDirectory(this) }
}
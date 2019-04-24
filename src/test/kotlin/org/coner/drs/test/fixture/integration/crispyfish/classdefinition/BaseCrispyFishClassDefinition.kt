package org.coner.drs.test.fixture.integration.crispyfish.classdefinition

import org.coner.crispyfish.filetype.classdefinition.ClassDefinitionFile
import java.io.File

abstract class BaseCrispyFishClassDefinition(val fileName: String) {

    private val folderName = "/org/coner/drs/test/fixture/integration/crispyfish/classdefinition"

    private val file = File(javaClass.getResource("$folderName/$fileName").toURI())

    val classDefinitionFile = ClassDefinitionFile(file)

    fun produceClassDefinitionFile(root: File): ClassDefinitionFile {
        check(root.isDirectory) { "root must be a directory" }
        val temporaryFile = file.copyTo(File(root, file.name))
        return ClassDefinitionFile(temporaryFile)
    }
}
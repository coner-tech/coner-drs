package org.coner.drs.test.fixture.integration.crispyfish.event

import org.coner.crispyfish.filetype.classdefinition.ClassDefinitionFile
import org.coner.crispyfish.filetype.ecf.EventControlFile
import org.coner.drs.test.fixture.integration.crispyfish.classdefinition.BaseCrispyFishClassDefinition
import java.io.File

abstract class BaseCrispyFishEvent(
        val eventControlFileName: String,
        val classDefinition: BaseCrispyFishClassDefinition
) {

    private val classNameLowercase = javaClass.simpleName.toLowerCase()
    private val folderName = "/org/coner/drs/test/fixture/integration/crispyfish/event/$classNameLowercase"

    val folder = File(javaClass.getResource(folderName).toURI())

    fun produceEventControlFile(root: File, classDefinitionFile: ClassDefinitionFile): EventControlFile {
        check(root.isDirectory) { "root must be a directory" }
        check(folder.copyRecursively(root)) { "failed to copy "}
        return EventControlFile(
                file = File(root, eventControlFileName),
                classDefinitionFile = classDefinitionFile,
                isTwoDayEvent = false,
                conePenalty = 2
        )
    }
}
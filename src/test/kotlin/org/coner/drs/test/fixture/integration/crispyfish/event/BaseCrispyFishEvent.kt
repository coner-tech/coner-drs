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
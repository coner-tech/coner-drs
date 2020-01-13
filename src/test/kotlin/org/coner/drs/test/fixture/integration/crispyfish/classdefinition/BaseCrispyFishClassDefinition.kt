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
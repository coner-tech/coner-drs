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

package org.coner.drs.test.fixture

import net.lingala.zip4j.ZipFile
import java.io.File
import java.util.*

sealed class TestEventFixture {
    private val name = this::class.simpleName!!.toLowerCase()
    private val source = File(javaClass.getResource("/org/coner/drs/test/fixture/$name.zip").toURI())

    abstract val eventIds: Array<UUID>

    fun factory(target: File): Instance {
        ZipFile(source).extractAll(target.absolutePath)
        return Instance(target.resolve(name), this)
    }

    class Instance(
            val root: File,
            val source: TestEventFixture
    ) {
        val crispyFishDatabase = root.resolve("cf-db")
        val digitalRawSheetDatabase = root.resolve("drs-db").toPath()
    }


    object Thscc2019Points9 : TestEventFixture() {
        override val eventIds = arrayOf(UUID.fromString("aed710cb-ef0a-4b7b-bce5-6f896ed536e1"))
    }
}
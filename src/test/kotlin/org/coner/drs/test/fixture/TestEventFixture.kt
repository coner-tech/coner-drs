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
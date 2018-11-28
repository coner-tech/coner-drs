package org.coner.drs.db

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.coner.drs.db.entity.EventDbEntity
import org.coner.snoozle.db.Database
import tornadofx.*
import java.io.File
import java.nio.file.Files

class DrsIoController : Controller() {

    var db: Database? = null
    val open: Boolean get() = db != null

    fun open(pathToDrsDatabase: File) {
        if (db != null) throw IllegalStateException("Database is already open")

        // TODO: Snoozle should auto-create these
        val pathToDrsDbPath = pathToDrsDatabase.toPath()
        if (Files.notExists(pathToDrsDbPath)) {
            Files.createDirectories(pathToDrsDbPath)
        }
        val pathToEvents = pathToDrsDatabase.toPath().resolve("events")
        if (Files.notExists(pathToEvents)) {
            Files.createDirectories(pathToEvents)
        }
        db = Database(
                root = pathToDrsDatabase,
                objectMapper = jacksonObjectMapper()
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .registerModule(JavaTimeModule()),
                entities = *arrayOf(
                        EventDbEntity::class
                )
        )
    }

    fun close() {
        db = null
    }
}

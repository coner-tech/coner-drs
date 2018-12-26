package org.coner.drs.io

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.coner.drs.Event
import org.coner.drs.io.db.entity.EventDbEntity
import org.coner.drs.io.db.entity.RunDbEntity
import org.coner.snoozle.db.Database
import tornadofx.*
import java.io.File
import java.nio.file.Files

class DrsIoController : Controller() {
    val model: DrsIoModel by inject()

    fun open(pathToDrsDatabase: File, pathToCrispyFishDatabase: File) {
        if (model.db != null) throw IllegalStateException("Database is already open")
        model.pathToDrsDatabase = pathToDrsDatabase
        model.pathToCrispyFishDatabase = pathToCrispyFishDatabase
        createDrsDbPath()
        createDrsDbEventsPath()
        model.db = Database(
                root = pathToDrsDatabase,
                objectMapper = jacksonObjectMapper()
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .enable(SerializationFeature.INDENT_OUTPUT)
                        .registerModule(JavaTimeModule())
                ,
                entities = *arrayOf(
                        EventDbEntity::class,
                        RunDbEntity::class
                )
        )
    }

    fun createDrsDbPath() {
        // TODO: Snoozle should auto-create these
        val pathToDrsDbPath = model.pathToDrsDatabase?.toPath()
        if (Files.notExists(pathToDrsDbPath)) {
            Files.createDirectories(pathToDrsDbPath)
        }
    }

    fun createDrsDbEventsPath() {
        // TODO: Snoozle should auto-create these
        val pathToEvents = model.pathToDrsDatabase?.toPath()
                ?.resolve("events")
        if (Files.notExists(pathToEvents)) {
            Files.createDirectories(pathToEvents)
        }
    }

    fun createDrsDbRunsPath(event: Event) {
        // TODO: Snoozle should auto-create these
        val pathToRuns = model.pathToDrsDatabase?.toPath()
                ?.resolve("events")
                ?.resolve(event.id.toString())
                ?.resolve("runs")
        if (Files.notExists(pathToRuns)) {
            Files.createDirectories(pathToRuns)
        }
    }

    fun isInsideCrispyFishDatabase(file: File): Boolean {
        return file.startsWith(model.pathToCrispyFishDatabase!!)
    }

    fun close() {
        model.db = null
        model.pathToDrsDatabase = null
    }
}

class DrsIoModel : ViewModel() {
    var db: Database? = null
    var pathToDrsDatabase: File? = null
    var pathToCrispyFishDatabase: File? = null
    val open: Boolean get() = db != null
}
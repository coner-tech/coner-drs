package org.coner.drs.ui.addevent

import javafx.stage.FileChooser
import javafx.util.StringConverter
import org.coner.drs.domain.model.EventCrispyFishMetadataModel
import org.coner.drs.domain.model.EventModel
import org.coner.drs.io.service.EventIoService
import org.coner.drs.util.requireFileWithinCrispyFishDatabase
import tornadofx.*
import java.io.File

class AddEventCrispyFishMetadataStepFragment : Fragment("Crispy Fish Metadata") {
    val event: EventModel by inject()
    val crispyFishMetadata: EventCrispyFishMetadataModel by inject()
    val controller: CreateEventCrispyFishMetadataStepController by inject()
    val eventService: EventIoService by inject()

    override val root = form {
        fieldset(title) {
            field("Event Control File") {
                textfield(
                        property = crispyFishMetadata.eventControlFile,
                        converter = CrispyFishDatabaseRelativeFileConverter()
                ) {
                    isEditable = false
                    required()
                    requireFileWithinCrispyFishDatabase()
                }
                button("Choose") {
                    action { controller.onClickChooseEventControlFile() }
                }
            }
            field("Class Definition File") {
                textfield(
                        property = crispyFishMetadata.classDefinitionFile,
                        converter = CrispyFishDatabaseRelativeFileConverter()
                ) {
                    isEditable = false
                    required()
                    requireFileWithinCrispyFishDatabase()
                }
                button("Choose") {
                    action { controller.onClickChooseClassDefinitionFile() }
                }
            }
        }
    }



    private inner class CrispyFishDatabaseRelativeFileConverter : StringConverter<File?>() {
        override fun toString(file: File?): String {
            return try {
                file?.toRelativeString(eventService.io.model.pathToCrispyFishDatabase!!)
                        ?: ""
            } catch (e: Throwable) {
                ""
            }
        }

        override fun fromString(file: String?): File {
            if (file == null) return File("")
            return File(eventService.io.model.pathToCrispyFishDatabase!!.resolve(file).absolutePath)
        }
    }

    override val complete = crispyFishMetadata.valid(
            crispyFishMetadata.eventControlFile,
            crispyFishMetadata.classDefinitionFile
    )

    override fun onSave() {
        super.onSave()
        event.item.crispyFishMetadata = crispyFishMetadata.item
    }
}

class CreateEventCrispyFishMetadataStepController : Controller() {
    val eventService: EventIoService by inject()
    val crispyFishMetadata: EventCrispyFishMetadataModel by inject()

    fun onClickChooseEventControlFile() {
        val crispyFishDatabase = eventService.io.model.pathToCrispyFishDatabase!!
        val file = chooseFile(
                title = "Choose Event Control File",
                filters = arrayOf(FileChooser.ExtensionFilter(
                        "Crispy Fish Event Control File", "*.ecf"
                )),
                owner = FX.primaryStage.owner,
                mode = FileChooserMode.Single
        ) {
            val currentFile = crispyFishMetadata.eventControlFile.value
            initialDirectory = if (currentFile?.startsWith(crispyFishDatabase) == true)
                currentFile.parentFile
            else
                crispyFishDatabase
        }.firstOrNull() ?: return
        crispyFishMetadata.eventControlFile.value = file
        crispyFishMetadata.commit(crispyFishMetadata.eventControlFile)
    }

    fun onClickChooseClassDefinitionFile() {
        val crispyFishDatabase = eventService.io.model.pathToCrispyFishDatabase!!
        val file = chooseFile(
                title = "Choose Crispy Fish Class Definition File",
                filters = arrayOf(FileChooser.ExtensionFilter(
                        "Crispy Fish Class Definition File", "*.def"
                )),
                owner = FX.primaryStage.owner,
                mode = FileChooserMode.Single
        ) {
            val currentFile = crispyFishMetadata.classDefinitionFile.value
            initialDirectory = if (currentFile?.startsWith(crispyFishDatabase) == true)
                currentFile.parentFile
            else
                crispyFishDatabase
        }.firstOrNull() ?: return
        crispyFishMetadata.classDefinitionFile.value = file
        crispyFishMetadata.commit(crispyFishMetadata.classDefinitionFile)
    }

}
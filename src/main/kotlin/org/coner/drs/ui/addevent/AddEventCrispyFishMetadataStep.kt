package org.coner.drs.ui.addevent

import javafx.beans.binding.StringBinding
import javafx.beans.value.ObservableValue
import javafx.stage.FileChooser
import org.coner.drs.Event
import org.coner.drs.EventModel
import org.coner.drs.io.service.EventService
import tornadofx.*
import java.io.File

class AddEventCrispyFishMetadataStepFragment : Fragment("Crispy Fish Metadata") {
    val event: EventModel by inject()
    val controller: CreateEventCrispyFishMetadataStepController by inject()
    val eventService: EventService by inject()

    override val root = form {
        fieldset(title) {
            field("Event Control File") {
                textfield(bindRelativePathToCrispyFishFile(event.crispyFishMetadata.value.eventControlFileProperty)) {
                    isEditable = false
                    validator(
                            control = this,
                            property = event.crispyFishMetadata,
                            trigger = ValidationTrigger.OnChange()
                    ) {
                        when {
                            it == null -> error("Field is required")
                            !eventService.io.isInsideCrispyFishDatabase(
                                    event.crispyFishMetadata.value.eventControlFile
                            ) -> {
                                error("File must be inside Crispy Fish Database")
                            }
                            else -> null
                        }
                    }
                }
                button("Choose") {
                    action { controller.onClickChooseEventControlFile() }
                }
            }
            field("Class Definitions File") {
                textfield(bindRelativePathToCrispyFishFile(event.crispyFishMetadata.value.classDefinitionFileProperty)) {
                    isEditable = false
                    validator(
                        control = this,
                        property = event.crispyFishMetadata,
                        trigger = ValidationTrigger.OnChange()
                    ) {
                        when {
                            it == null -> error("Field is required")
                            !eventService.io.isInsideCrispyFishDatabase(
                                    event.crispyFishMetadata.value.classDefinitionFile
                            ) -> {
                                error("File must be inside Crispy Fish Database")
                            }
                            else -> null
                        }
                    }
                }
                button("Choose") {
                    action { controller.onClickChooseClassDefinitionsFile() }
                }
            }
        }
    }

    fun bindRelativePathToCrispyFishFile(fileValue: ObservableValue<File>): StringBinding {
        return fileValue.stringBinding {
            try {
                it?.toRelativeString(eventService.io.model.pathToCrispyFishDatabase!!)
            } catch (e: Throwable) {
                ""
            }
        }
    }

    override val complete = event.valid(
            event.crispyFishMetadata // TODO: find out if/why this isn't updating
    )
}

class CreateEventCrispyFishMetadataStepController : Controller() {
    val event: EventModel by inject()
    val eventService: EventService by inject()

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
            val currentFile = event.crispyFishMetadata.value.eventControlFile
            initialDirectory = if (currentFile?.startsWith(crispyFishDatabase) == true)
                currentFile.parentFile
            else
                crispyFishDatabase
        }.firstOrNull() ?: return
        event.crispyFishMetadata.value.eventControlFile = file
        event.validate(fields = *arrayOf(event.crispyFishMetadata))
    }

    fun onClickChooseClassDefinitionsFile() {
        val crispyFishDatabase = eventService.io.model.pathToCrispyFishDatabase!!
        val file = chooseFile(
                title = "Choose Crispy Fish Class Definitions File",
                filters = arrayOf(FileChooser.ExtensionFilter(
                        "Crispy Fish Class Definitions", "*.def"
                )),
                owner = FX.primaryStage.owner,
                mode = FileChooserMode.Single
        ) {
            val currentFile = event.crispyFishMetadata.value.classDefinitionFile
            initialDirectory = if (currentFile?.startsWith(crispyFishDatabase) == true)
                currentFile.parentFile
            else
                crispyFishDatabase
        }.firstOrNull() ?: return
        event.crispyFishMetadata.value.classDefinitionFile = file
        event.validate(fields = *arrayOf(event.crispyFishMetadata))
    }

}
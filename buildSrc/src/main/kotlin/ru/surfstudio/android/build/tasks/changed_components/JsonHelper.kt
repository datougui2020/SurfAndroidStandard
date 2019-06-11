package ru.surfstudio.android.build.tasks.changed_components

import com.google.gson.GsonBuilder
import ru.surfstudio.android.build.Folders.COMPONENTS_JSON_FILE_PATH
import ru.surfstudio.android.build.model.json.ComponentJson
import ru.surfstudio.android.build.tasks.changed_components.models.ProjectConfiguration
import java.io.File

/**
 * helper for parsing info configuration files
 */
object JsonHelper {

    val gson = GsonBuilder().setPrettyPrinting().create()

    fun parseProjectConfigurationFile(path: String): ProjectConfiguration {
        return gson.fromJson(File(path).reader(), ProjectConfiguration::class.java)
    }

    fun writeProjectConfigurationFile(project: ProjectConfiguration, file: File) {
        file.writeText(gson.toJson(project))
    }

    fun writeComponentsFile(components: List<ComponentJson>, file: File) {
        file.writeText(gson.toJson(components))
    }

    fun parseComponentsJson(pathToProject: String): List<ComponentJson> {
        return GsonBuilder()
                .create()
                .fromJson(File("$pathToProject$COMPONENTS_JSON_FILE_PATH").reader(), Array<ComponentJson>::class.java)
                .toList()
    }
}
package ru.surfstudio.android.build.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.artifactory.Artifactory
import ru.surfstudio.android.build.utils.getComponent

/**
 * Check artifact for android standard dependencies exist in artifactory
 */
open class CheckExistingDependencyArtifactsInArtifactoryTask : DefaultTask() {

    @TaskAction
    fun check() {
        val component = project.getComponent()

        Artifactory.checkLibrariesStandardDependenciesExisting(component)
    }
}
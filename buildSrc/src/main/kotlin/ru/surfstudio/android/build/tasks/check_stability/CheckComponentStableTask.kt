package ru.surfstudio.android.build.tasks.check_stability

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.exceptions.component.ComponentUnstableException
import ru.surfstudio.android.build.utils.getPropertyComponent

/**
 * Check component stable
 */
open class CheckComponentStableTask : DefaultTask() {

    /**
     * Check component stable
     */
    @TaskAction
    fun check() {
        val component = project.getPropertyComponent()

        if (!component.stable) throw ComponentUnstableException(component.name)
    }
}
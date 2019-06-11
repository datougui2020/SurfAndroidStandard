package ru.surfstudio.android.build

import org.gradle.api.Project
import ru.surfstudio.android.build.exceptions.ComponentNotFoundForStandardDependencyException
import ru.surfstudio.android.build.exceptions.LibraryNotFoundException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.model.dependency.Dependency
import ru.surfstudio.android.build.model.module.Library
import ru.surfstudio.android.build.model.module.Module
import ru.surfstudio.android.build.model.json.ComponentJson
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.getProjectSnapshot

/**
 * Project value
 */
object Components {

    var value: List<Component> = emptyList()
        set(value) {
            field = value
            libraries = value.flatMap { it.libraries }
        }

    var libraries: List<Library> = emptyList()

    /**
     * Create value from json value
     */
    fun init(componentJsons: List<ComponentJson>) {
        value = componentJsons.map(ComponentJson::transform)
        setComponentsForAndroidStandardDependencies()
    }

    /**
     * Get project's module
     */
    @JvmStatic
    fun getModules(): List<Module> = value.flatMap(Component::getModules)

    /**
     * Get moduleVersionName
     *
     * There are 4 types of version:
     * 1. X.Y.Z - component is stable, projectPostfix is empty
     * 2. X.Y.Z-alpha.unstable_version - component is unstable, projectPostfix is empty
     * 3. X.Y.Z-projectPostfix.projectVersion - component is stable, projectPostfix isn't empty
     * 4. X.Y.Z-alpha.unstable_version-projectPostfix.projectVersion - component is unstable, projectPostfix isn't empty
     */
    @JvmStatic
    fun getModuleVersion(gradleProject: Project): String {
        if (value.isEmpty()) return EMPTY_STRING
        if (value.any { it.projectVersion.isEmpty() }) configModuleVersions(gradleProject)

        val moduleName = gradleProject.name
        val component = value.find { it.getModules().map(Module::name).contains(moduleName) }

        return component?.projectVersion ?: EMPTY_STRING
    }

    /**
     * Get artifact name for library
     *
     * @param libraryName - library name
     */
    @JvmStatic
    fun getArtifactName(libraryName: String): String {
        val library = value.flatMap { it.libraries }.find { it.name == libraryName }
        return library?.artifactName ?: EMPTY_STRING
    }

    /**
     * Get standard artifact names by library name
     */
    @JvmStatic
    fun getAndroidStandardDependencies(libraryName: String): List<Library> {
        val libs = value.flatMap { it.libraries }
        val standardDepNames = libs.find { it.name == libraryName }
                ?.androidStandardDependencies
                ?.map(Dependency::name) ?: return emptyList()
        return libs.filter { standardDepNames.contains(it.name) }
    }

    /**
     * Get component stability by module name
     */
    @JvmStatic
    fun getComponentStability(libraryName: String): Boolean {
        value.forEach { component ->
            component.libraries
                    .find { it.name == libraryName }
                    ?.let { return component.stable }
        }

        throw LibraryNotFoundException(libraryName)
    }

    /**
     * Set components for android standard dependencies
     */
    private fun setComponentsForAndroidStandardDependencies() {
        val libs = value.flatMap { it.libraries }
        val libNameCompMap: Map<String, Component?> = libs.map { lib ->
            lib.name to value.find { it.libraries.contains(lib) }
        }.toMap()

        value.forEach { component ->
            component.libraries.forEach { library ->
                library.androidStandardDependencies.forEach { dependency ->
                    dependency.component = libNameCompMap[dependency.name]
                            ?: throw ComponentNotFoundForStandardDependencyException(dependency.name)
                }
            }
        }
    }

    /**
     * Create project versions for components
     */
    private fun configModuleVersions(gradleProject: Project) {
        val projectSnapshot = gradleProject.getProjectSnapshot()
        value.forEach { component ->
            var versionName = component.baseVersion

            if (!component.stable) versionName += "-alpha.${component.unstableVersion}"
            if (!projectSnapshot.isEmpty) versionName += "-${projectSnapshot.name}.${projectSnapshot.version}"

            component.projectVersion = versionName
        }
    }
}
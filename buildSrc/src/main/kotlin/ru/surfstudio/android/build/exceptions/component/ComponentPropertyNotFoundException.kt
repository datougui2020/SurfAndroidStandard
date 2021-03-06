package ru.surfstudio.android.build.exceptions.component

import org.gradle.api.GradleException

/**
 * Component property not found
 */
class ComponentPropertyNotFoundException : GradleException(
        "Component property doesn't found. Please define : -Pcomponent=<component_name>"
)
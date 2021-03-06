package ru.surfstudio.android.build.exceptions.release_notes

import org.gradle.api.GradleException

class ReleaseNotesForFilesException(
        componentName: String,
        reason: String
) : GradleException(
        "Component $componentName was changed but its file release notes file was not changed. Because $reason"
)
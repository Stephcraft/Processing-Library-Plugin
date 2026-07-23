package net.stephcraft.processing.library

import org.gradle.api.Action
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.OutputDirectory
import net.stephcraft.processing.library.LibraryCategory.*

abstract class ProcessingLibraryExtension: LibraryCategory.Import() {

    @get:Nested
    abstract val properties: ProcessingLibraryProperties

    @get:InputFile
    abstract val propertiesFile: RegularFileProperty

    @get:OutputDirectory
    abstract val destinationDirectory: DirectoryProperty

    fun properties(action: Action<in ProcessingLibraryProperties>) {
        action.execute(properties)
    }
}
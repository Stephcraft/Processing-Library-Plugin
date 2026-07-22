package net.stephcraft.processing.library

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

abstract class LibraryAuthor {

    @get:Input
    abstract val name: Property<String>

    @get:Input
    abstract val website: Property<String>

    override fun toString(): String {
        return when(val website = website.orNull) {
            null -> name.get()
            else -> "[${name.get()}](${website})"
        }
    }
}
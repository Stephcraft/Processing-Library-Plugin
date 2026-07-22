package net.stephcraft.processing.library

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

abstract class LibraryAuthorContainer @Inject constructor(private val objects: ObjectFactory) {
    val items = mutableListOf<LibraryAuthor>()

    /** Configure author */
    fun author(action: Action<LibraryAuthor>) {
        val license = objects.newInstance(LibraryAuthor::class.java)
        action.execute(license)
        items.add(license)
    }

    override fun toString(): String {
        return items.joinWithAnd()
    }

    private fun List<Any>.joinWithAnd(): String = when (size) {
        0 -> ""
        1 -> first().toString()
        2 -> "${first()} and ${this[1]}"
        else -> "${dropLast(1).joinToString(", ")}, and ${last()}"
    }
}
package net.stephcraft.processing.library

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import javax.inject.Inject

/**
 * Configures the Processing Library properties
 */
abstract class ProcessingLibraryProperties @Inject constructor(private val objects: ObjectFactory) {

    /** The name of your library as you want it formatted. */
    @get:Input
    abstract val name: Property<String>

    /** List of authors. */
    @get:Input
    abstract val authors: Property<String>

    /** Configure authors */
    fun authors(action: Action<LibraryAuthorContainer>) {
        val container = objects.newInstance(LibraryAuthorContainer::class.java)
        action.execute(container)
        authors.set(container.toString())
    }

    /** Configure solo author */
    fun author(action: Action<LibraryAuthor>) {
        val container = objects.newInstance(LibraryAuthor::class.java)
        action.execute(container)
        authors.set(container.toString())
    }

    /** A web page for your library, NOT a direct link to where to download it. */
    @get:Input
    abstract val url: Property<String>

    /** The category (or categories) of your library. */
    @get:Input
    abstract val categories: SetProperty<LibraryCategory>

    /**
     * A short sentence (or fragment) to summarize the library's function. This will
     * be shown from inside the PDE when the library is being installed. Avoid
     * repeating the name of your library here. Also, avoid saying anything redundant
     * like mentioning that it's a library. This should start with a capitalized
     * letter, and end with a period.
     *
     * Links can be inserted using the following syntax:
     * `[here is a link to Processing](http://processing.org/)`
     */
    @get:Input
    abstract val sentence: Property<String>

    /**
     * Additional information suitable for the Processing website. The value of
     * 'sentence' will always be prepended, so you should start by writing the
     * second sentence here. If your library only works on certain operating systems,
     * mention it here.
     *
     * Links can be inserted using the following syntax:
     * `[here is a link to Processing](http://processing.org/)`
     */
    @get:Input
    abstract val paragraph: Property<String>

    /**
     * A version number that increments once with each release. This is used to
     * compare different versions of the same library, and check if an update is
     * available. You should think of it as a counter, counting the total number of
     * releases you've had.
     */
    @get:Input
    abstract val version: Property<Int>

    /**
     * The version as the user will see it. If blank, the version attribute will be
     * used here. This should be a single word, with no spaces.
     */
    @get:Input
    abstract val prettyVersion: Property<String>

    /**
     * The min and max revision of Processing compatible with your library.
     * Note that these fields use the revision and not the version of Processing,
     * parsable as an int. For example, the revision number for 2.2.1 is 227.
     * You can find the revision numbers in the release tags:
     * [Processing 4 tags](https://github.com/processing/processing4/tags)
     * Only use maxRevision (or minRevision), when your library is known to
     * break in a later (or earlier) release. Otherwise, use the default value 0.
     */
    @get:Internal
    var revisionRange: IntRange
        get() = minRevision.get()..maxRevision.get()
        set(range) {
            minRevision.set(range.first)
            maxRevision.set(range.last)
        }

    @get:Input
    abstract val minRevision: Property<Int>

    @get:Input
    abstract val maxRevision: Property<Int>
}
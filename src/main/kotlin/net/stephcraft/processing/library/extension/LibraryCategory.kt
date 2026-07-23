package net.stephcraft.processing.library

/**
 * Represents the Processing Library categories
 *
 * [Processing Libraries](https://processing.org/reference/libraries)
 */
enum class LibraryCategory(val value: String) {
    THREE_DIMENSIONAL("3D"),    ANIMATION("Animation"), COMPILATIONS("Compilations"), DATA("Data"),
    FABRICATION("Fabrication"), GEOMETRY("Geometry"),   GUI("GUI"),                   HARDWARE("Hardware"),
    IO("I/O"),                  LANGUAGE("Language"),   MATH("Math"),                 SIMULATION("Simulation"),
    SOUND("Sound"),             UTILITIES("Utilities"), TYPOGRAPHY("Typography"),     VIDEO_VISION("Video & Vision");

    operator fun plus(other: LibraryCategory) = setOf(this, other)

    /**
     * Avoid `import net.stephcraft.processing.library.LibraryCategory.*` in build.gradle.kts
     * TODO: is this the best way?
     */
    open class Import {
        val THREE_DIMENSIONAL = LibraryCategory.THREE_DIMENSIONAL
        val ANIMATION         = LibraryCategory.ANIMATION
        val COMPILATIONS      = LibraryCategory.COMPILATIONS
        val DATA              = LibraryCategory.DATA

        val FABRICATION       = LibraryCategory.FABRICATION
        val GEOMETRY          = LibraryCategory.GEOMETRY
        val GUI               = LibraryCategory.GUI
        val HARDWARE          = LibraryCategory.HARDWARE

        val IO                = LibraryCategory.IO
        val LANGUAGE          = LibraryCategory.LANGUAGE
        val MATH              = LibraryCategory.MATH
        val SIMULATION        = LibraryCategory.SIMULATION

        val SOUND             = LibraryCategory.SOUND
        val UTILITIES         = LibraryCategory.UTILITIES
        val TYPOGRAPHY        = LibraryCategory.TYPOGRAPHY
        val VIDEO_VISION      = LibraryCategory.VIDEO_VISION
    }

    companion object {

        fun parse(value: String): LibraryCategory? {
            return try {  enumValueOf<LibraryCategory>(value.trim().uppercase()) }
            catch(exception: Exception) { null }
        }
    }
}
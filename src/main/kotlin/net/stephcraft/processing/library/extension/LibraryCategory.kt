package net.stephcraft.processing.library

import org.gradle.api.Project

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

    companion object {

        fun parse(value: String): LibraryCategory? {
            return try {  enumValueOf<LibraryCategory>(value.trim().uppercase()) }
            catch(exception: Exception) { null }
        }
    }
}
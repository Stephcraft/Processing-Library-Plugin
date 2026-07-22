import net.stephcraft.processing.library.LibraryCategory

group = "net.stephcraft.processing"
version = "0.0.0"

plugins {
    /** https://kotlinlang.org/docs/releases.html */
    kotlin("jvm") version "2.4.0"
    id("net.stephcraft.processing-library")
}

processingLibrary {

    properties {

        name = "Events"
        url = "https://stephcraft.net/events"
        sentence = ""
        paragraph = ""
        categories = setOf(LibraryCategory.UTILITIES)
        version = 1
        prettyVersion = project.version.toString()
        revisionRange = 0..1000

        authors {
            author {
                name = "Stephcraft"
                website = "https://stephcraft.net"
            }
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    /** https://mvnrepository.com/artifact/org.processing/core */
    implementation("org.processing:core:4.5.5")
}

tasks {
    generateProcessingLibrary {
        mustRunAfter("jar")
    }
}
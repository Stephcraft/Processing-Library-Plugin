group = "net.stephcraft.processing"
version = "0.0.1"

plugins {
    /** https://kotlinlang.org/docs/releases.html */
    kotlin("jvm") version "2.4.0"
    /** https://kotlinlang.org/docs/sam-with-receiver-plugin.html */
    kotlin("plugin.sam.with.receiver") version "2.4.0"
    /** https://plugins.gradle.org/plugin/com.gradle.plugin-publish */
    id("com.gradle.plugin-publish") version "2.1.0"
    /** https://plugins.gradle.org/plugin/org.jetbrains.dokka */
    id("org.jetbrains.dokka") version "2.2.0"
    /** https://plugins.gradle.org/plugin/org.jetbrains.dokka-javadoc */
    id("org.jetbrains.dokka-javadoc") version "2.2.0"

    `java-gradle-plugin`
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

/**
 * Gradle Plugin Configuration
 * https://stephcraft.net/docs/gradle/#plugin-development-build-script
 */
gradlePlugin {
    website = "https://github.com/Stephcraft/Processing-Library-Plugin"
    vcsUrl = "https://github.com/Stephcraft/Processing-Library-Plugin.git"

    plugins {
        val processingLibraryPlugin by registering {
            id = "net.stephcraft.processing-library"
            implementationClass = "$group.library.ProcessingLibraryPlugin"

            displayName = "Processing Library plugin"
            description = "Gradle plugin for Processing Library development"
            tags = setOf("processing", "processing-library")
        }
    }
}

/**
 * Adds proper Gradle Kotlin DSL
 * { this } instead of { it }
 */
samWithReceiver {
    /** https://docs.gradle.org/current/kotlin-dsl/gradle/org.gradle.api/-has-implicit-receiver/index.html */
    annotation("org.gradle.api.HasImplicitReceiver")
}

repositories {
    mavenCentral()
}
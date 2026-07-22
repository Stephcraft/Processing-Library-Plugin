package net.stephcraft.processing.library

import net.stephcraft.processing.library.task.ProcessingLibraryTask
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Project.DEFAULT_VERSION
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.WriteProperties
import org.gradle.api.tasks.bundling.Zip
import java.util.Properties

class ProcessingLibraryPlugin: Plugin<Project> {

    override fun apply(project: Project): Unit = with(project) {

        // processingLibrary { .. }
        val extension = extensions.create<ProcessingLibraryExtension>("processingLibrary") {
            propertiesFile.convention(
                layout.projectDirectory.file("library.properties")
            )
            destinationDirectory.convention(
                layout.buildDirectory.dir("processingLibrary")
            )
        }

        // library.properties
        val properties = providers.fileContents(extension.propertiesFile)
            .asText.map { text -> Properties().apply { load(text.reader()) } }

        // load properties
        extension.properties.apply {
            name.convention(
                properties
                    .map { it.getProperty("name") }
                    .orElse(provider { project.name })
            )
            authors.convention(
                properties
                    .map { it.getProperty("authors") }
                    .orElse(System.getProperty("user.name"))
            )
            url.convention(
                properties
                    .map { it.getProperty("url") }
                    .orElse("")
            )
            categories.convention(
                properties.map {
                    it.getProperty("categories").split(",").mapNotNull(LibraryCategory::parse)
                }.orElse(listOf())
            )
            sentence.convention(
                properties
                    .map { it.getProperty("sentence") }
                    .orElse(provider { project.description })
                    .orElse("")
            )
            paragraph.convention(
                properties
                    .map { it.getProperty("paragraph") }
                    .orElse("")
            )
            version.convention(
                properties
                    .map { it.getProperty("version")?.toIntOrNull() }
                    .orElse(0)
            )
            prettyVersion.convention(
                properties
                    .map { it.getProperty("prettyVersion") }
                    .orElse(provider {
                        project.version.toString().takeIf { it != DEFAULT_VERSION }
                    })
                    .orElse("")
            )
            minRevision.convention(
                properties
                    .map { it.getProperty("minRevision")?.toIntOrNull() }
                    .orElse(0)
            )
            maxRevision.convention(
                properties
                    .map { it.getProperty("maxRevision")?.toIntOrNull() }
                    .orElse(0)
            )
        }

        //#region tasks

        /**
         * generate txt in build/processingLibrary/{name}.txt
         */
        val propertiesTask = tasks.register<WriteProperties>("generateProcessingLibraryProperties") {
            group = "processing"
            description = "Generates the Processing Library's library.properties"

            destinationFile.set(
                extension.destinationDirectory.flatMap { directory ->
                    extension.properties.name.map { libraryName ->
                        directory.file("$libraryName.txt")
                    }
                }
            )

            //#region task
            with(extension.properties) {
                property("name", name)
                property("authors", authors)
                property("version", version)
                property("prettyVersion", prettyVersion)
                property("url", url)
                property("categories", categories.map { it.joinToString(",") { it.value } })
                property("sentence", sentence)
                property("paragraph", paragraph)
                property("minRevision", minRevision)
                property("maxRevision", maxRevision)
            }
            //#endregion
        }

        /**
         * generate library files in build/processingLibrary/{name}
         */
        val unwrappedTask = tasks.register<ProcessingLibraryTask>("generateProcessingLibraryUnwrapped") {
            group = "processing"
            description = "Generates the unpacked Processing Library"

            dependsOn("jar", "javadoc")
            dependsOn("generateProcessingLibraryProperties")

            destinationDirectory.set(
                extension.destinationDirectory.flatMap { directory ->
                    extension.properties.name.map { libraryName ->
                        directory.dir(libraryName)
                    }
                }
            )

            examplesDirectory.set(
                layout.projectDirectory.dir("examples")
            )

            propertiesFile.set(
                propertiesTask.flatMap { it.destinationFile }
            )
        }

        /**
         * zip library files in build/processingLibrary/{name}.zip
         */
        val archiveTask = tasks.register<Zip>("generateProcessingLibraryArchive") {
            group = "processing"
            description = "Generates the Processing Library artifacts"

            dependsOn("generateProcessingLibraryUnwrapped")

            archiveFileName.set(
                extension.properties.name.map { libraryName ->
                    "$libraryName.zip"
                }
            )

            destinationDirectory.set(extension.destinationDirectory)

            //#region task
            from(unwrappedTask.flatMap { it.destinationDirectory })
            exclude("**/*.DS_Store")
            //#endregion
        }

        /**
         * pdex in build/processingLibrary/{name}.pdex
         */
        tasks.register<Copy>("generateProcessingLibraryPdex") {
            group = "processing"
            description = "Generates the Processing Library's .pdex"

            dependsOn("generateProcessingLibraryArchive")

            //#region task
            from(archiveTask.flatMap { it.archiveFile }) {
                rename { it.replace(".zip", ".pdex") }
            }

            into(extension.destinationDirectory)
            //#endregion
        }

        /**
         * Final task
         */
        tasks.register<DefaultTask>("generateProcessingLibrary") {
            group = "processing"
            description = "Generates the Processing Library"

            dependsOn(
                "generateProcessingLibraryProperties",
                "generateProcessingLibraryUnwrapped",
                "generateProcessingLibraryArchive",
                "generateProcessingLibraryPdex"
            )
        }

        //#endregion
    }

    //#region kotlin gradle dsl utilities

    inline fun <reified T: Any> ExtensionContainer.create(name: String, noinline block: T.() -> Unit) = (
        create(name, T::class.java).apply(block)
    )

    inline fun <reified T: Task> TaskContainer.register(name: String, noinline block: T.() -> Unit) = (
        register(name, T::class.java, block)
    )

    context(project: Project)
    fun <T: Any> Property<T>.convention(block: () -> T?) {
        convention(
            project.providers.provider(block)
        )
    }

    //#endregion
}
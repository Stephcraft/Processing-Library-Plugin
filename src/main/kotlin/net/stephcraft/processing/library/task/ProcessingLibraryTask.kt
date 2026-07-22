package net.stephcraft.processing.library.task

import net.stephcraft.processing.library.ProcessingLibraryProperties
import org.gradle.api.Action
import org.gradle.api.DefaultTask
import org.gradle.api.file.CopySpec
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.WorkResult
import org.gradle.jvm.tasks.Jar
import java.io.File
import java.io.FileInputStream
import java.util.Properties
import javax.inject.Inject
import kotlin.io.resolve
import kotlin.text.get

abstract class ProcessingLibraryTask @Inject constructor(
    private val fileSystemOperations: FileSystemOperations
): DefaultTask() {

    @get:InputFile
    abstract val propertiesFile: RegularFileProperty

    @get:Internal
    // @get:Optional
    abstract val examplesDirectory: DirectoryProperty

    @get:OutputDirectory
    abstract val destinationDirectory: DirectoryProperty

    @get:Internal
    val sourceDirectorySet = project.provider {
        val sourceSets = project.extensions.getByType(SourceSetContainer::class.java)
        sourceSets.named(SourceSet.MAIN_SOURCE_SET_NAME).get().allSource
    }

    @get:Internal
    val referenceDirectory = project.provider {
        project.layout.buildDirectory.dir("docs/javadoc")
    }

    @get:Internal
    val libraryJarFile = project.provider {
        project.tasks.named("jar", Jar::class.java).get().archiveFile
    }

    @get:Internal
    val dependencyJarFiles = project.provider {
        project.configurations.named("runtimeClasspath")
    }

    @TaskAction
    fun copyFiles() {
        val destinationDirectory = destinationDirectory.asFile.get()

        with(fileSystemOperations) {

            // copy source/
            copy {
                into(destinationDirectory.resolve("source"))
                from(sourceDirectorySet.get())
            }

            // copy reference/
            copy {
                into(destinationDirectory.resolve("reference"))
                from(referenceDirectory)
            }

            // copy examples/
            copy {
                into(destinationDirectory.resolve("examples"))

                // only take processing sketch examples
                val exampleSketches = run {
                    examplesDirectory.asFile.orNull?.listFiles()
                        ?.filter { dir ->
                            dir.isDirectory &&
                            dir.listFiles()?.any { it.extension == "pde" } == true
                        } ?: emptyList()
                }
                from(exampleSketches)
            }

            // copy library/ *.jar
            copy {
                into(destinationDirectory.resolve("library"))

                // library.jar
                from(libraryJarFile)

                // dependencies *.jar
                from(dependencyJarFiles)
            }

            // copy library.properties
            copy {
                into("library.properties")
                from(propertiesFile)
            }

            // copy README.md & LICENSE
            copy {
                into(destinationDirectory)
                from("README.md")
                from("LICENSE")
            }
        }
    }
}
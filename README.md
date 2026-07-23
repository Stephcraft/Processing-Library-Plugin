# <img src="https://i.imgur.com/FK4arV7.png" height="26"/> Processing Library Plugin
[![Processing](https://img.shields.io/static/v1?style=flat&logo=processingfoundation&label=&message=Processing&logoColor=white&labelColor=333&color=444)](https://processing.org)
[![Gradle](https://img.shields.io/static/v1?style=flat&logo=gradle&label=&message=Gradle&logoColor=white&labelColor=333&color=444)](https://stephcraft.itch.io/pacman)

Gradle Plugin to build Processing Libraries.

## Processing Library Reference
- [**Processing** — Libraries](https://processing.org/reference/libraries)
- [**Processing Wiki** — Library Guidelines](https://github.com/processing/processing4/wiki/Library-Guidelines)
- [**Processing Wiki** — `library.properties`](https://github.com/processing/processing4/wiki/Library-Basics#describing-your-library-with-libraryproperties)

## Usage

<img src="https://i.imgur.com/F86XfPz.png" height="12"/> **Download** <img src="https://i.imgur.com/BpJQtY7.png" height="12"/> [IntelliJ IDEA](https://www.jetbrains.com/idea)

### <img src="https://intellij-icons.jetbrains.design/icons/AllIcons/expui/actions/projectDirectory.svg"/> Project Layout
<img src="https://intellij-icons.jetbrains.design/icons/AllIcons/expui/nodes/module.svg" height="14"/> **`LibraryName`**  
├─ <img src="https://intellij-icons.jetbrains.design/icons/KotlinBaseResourcesIcons/org/jetbrains/kotlin/idea/icons/expui/kotlinGradleScript.svg" height="14"/> `build.gradle.kts`  
├─ <img src="https://intellij-icons.jetbrains.design/icons/AllIcons/expui/nodes/sourceRoot.svg" height="14"/> `src/java/main`  
 │‎  └─ <img src="https://intellij-icons.jetbrains.design/icons/AllIcons/expui/nodes/package.svg" height="14"/> `me.username`  
 │‎     └─ <img src="https://intellij-icons.jetbrains.design/icons/AllIcons/expui/nodes/class.svg" height="14"/> `ProcessingLibrary.java`  
└─ <img src="https://intellij-icons.jetbrains.design/icons/AllIcons/expui/nodes/excludeRoot.svg" height="14"/> `build/processingLibrary`  
   ├─ <img src="https://i.imgur.com/PjruJzq.png" height="14"/> `LibraryName.pdex`  
   ├─ <img src="https://intellij-icons.jetbrains.design/icons/AllIcons/expui/fileTypes/archive.svg" height="14"/> `LibraryName.zip`  
   └─ <img src="https://intellij-icons.jetbrains.design/icons/AllIcons/expui/fileTypes/text.svg" height="14"/> `LibraryName.txt`  

### <img src="https://intellij-icons.jetbrains.design/icons/KotlinBaseResourcesIcons/org/jetbrains/kotlin/idea/icons/expui/kotlinGradleScript.svg"/> Build Script
`build.gradle.kts`
```gradle
group = "me.username"
version = "0.0.1"

plugins {
    java
    
    /** Apply the Processing Library Gradle Plugin */
    id("net.stephcraft.processing-library") version "0.0.1"
}

processingLibrary {

    /** Configure library.properties here */
    properties {
        /** The name of your library as you want it formatted. (defaults to project name) */
        name = "Cool Library"
        /** A web page for your library, NOT a direct link to where to download it. */
        url = "https://username.github.io/libraryName"
        /** The category (or categories) of your library. */
        categories = setOf(LibraryCategory.UTILITIES)
        /** A short sentence to summarize the library's function. (defaults to project description) */
        sentence = ""
        /** Additional information suitable for the Processing website. */
        paragraph = ""
        /** A version number that increments once with each release. */
        version = 1
        /** The version as the user will see it. (defaults to project version) */
        prettyVersion = project.version.toString()
        /** The min and max revision of Processing compatible with your library. (optional) */
        revisionRange = 0..1000
    }

    /** Alternatively you can use a library.properties file */
    propertiesFile = file("library.properties")

    /** You can also customize the output directory */
    destinationDirectory = layout.buildDirectory.dir("processingLibrary")
}

repositories {
    mavenCentral()
}

dependencies {
    /**
     * Add Processing as a dependency
     * https://mvnrepository.com/artifact/org.processing/core
     */
    compileOnly("org.processing:core:4.5.5")
}

tasks {
    /**
     * :generateProcessingLibrary will run when you press Build in IntelliJ IDEA
     * The generated processing library files are in build/processingLibrary
     */
    generateProcessingLibrary {
        mustRunAfter("jar")
    }
}
```

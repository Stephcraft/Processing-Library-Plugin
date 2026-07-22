# Processing Library Plugin
[![Processing](https://img.shields.io/static/v1?style=flat&logo=processingfoundation&label=&message=Processing&logoColor=white&labelColor=333&color=444)](https://processing.org)
[![Gradle](https://img.shields.io/static/v1?style=flat&logo=gradle&label=&message=Gradle&logoColor=white&labelColor=333&color=444)](https://stephcraft.itch.io/pacman)

## Reference
- [**Processing Wiki** — Library Guidelines](https://github.com/processing/processing4/wiki/Library-Guidelines)
- [**Processing Wiki** — `library.properties`](https://github.com/processing/processing4/wiki/Library-Basics#describing-your-library-with-libraryproperties)

## Usage

### Project Layout
```
├─ build.gradle.kts
├─ src/java/main
│  └─ me.username
│     └─ ProcessingLibrary.java
└─ build/processingLibrary
   └─ {LibraryName}.zip
   └─ {LibraryName}.pdex
   └─ {LibraryName}.txt
```

### Build Script
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

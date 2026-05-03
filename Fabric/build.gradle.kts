import fuzs.multiloader.extension.mod
import fuzs.multiloader.extension.packageName

plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-fabric")
}

dependencies {
    modLocalRuntime(sharedLibs.fabricapi.fabric)
}

multiloader {
    modFile {
        library.set(true)
        json {
            languageAdapters.put(
                project.mod.id,
                "${project.group}.${project.packageName}.ConfiguredDefaultsLanguageAdapter"
            )
        }
    }
}

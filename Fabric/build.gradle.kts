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
                "${project.group}.${project.name.lowercase()}.ConfiguredDefaultsLanguageAdapter"
            )
        }
    }
}

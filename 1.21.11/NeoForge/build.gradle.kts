plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-neoforge")
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes("FMLModType" to "LIBRARY")
    }
}

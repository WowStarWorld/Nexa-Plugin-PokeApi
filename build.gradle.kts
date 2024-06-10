@file:Suppress("VulnerableLibrariesLocal")

plugins {
    kotlin("jvm") version "2.0.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "pixel.nexa.plugin.pokeapi"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        name = "JitPack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    testImplementation(kotlin("test"))

    compileOnly("com.github.PixelVoyagers.Nexa:nexa-core:b9aca04449")
    compileOnly("com.squareup.retrofit2:retrofit:2.11.0")

    api("com.triceracode:poke-api:1.0.0")

}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<ProcessResources> {
    val resourceTargets = listOf("plugin.yml")
    val replaceProperties = mapOf(
        Pair(
            "gradle",
            mapOf(
                Pair("gradle", gradle),
                Pair("project", project)
            )
        )
    )
    filesMatching(resourceTargets) {
        expand(replaceProperties)
    }
}

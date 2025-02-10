plugins {
    kotlin("jvm") version "2.0.21"
    //id("org.jetbrains.dokka") version "1.9.0"
}

group = "org.sportradar.lib"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

tasks.test {
    useJUnitPlatform()
}

//tasks.dokkaHtml {
//    outputDirectory.set(file("build/docs/dokka"))
//}

kotlin {
    jvmToolchain(17)
}
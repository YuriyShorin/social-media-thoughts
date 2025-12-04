plugins {
    id("org.jetbrains.kotlin.jvm") version "2.2.21"
    id("org.jlleitschuh.gradle.ktlint") version "14.0.1"
}

allprojects {
    repositories {
        gradlePluginPortal()
    }

    apply(plugin = "org.jlleitschuh.gradle.ktlint")
}

group = "ru.shorin"
version = "0.0.1"

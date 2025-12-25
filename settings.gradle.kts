pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        id("org.jetbrains.kotlin.jvm") version "2.3.0"
        id("org.jetbrains.kotlin.plugin.spring") version "2.3.0"
        id("org.jetbrains.kotlin.plugin.jpa") version "2.3.0"
        id("org.jetbrains.kotlin.kapt") version "2.3.0"
        id("org.springframework.boot") version "4.0.1"
        id("io.spring.dependency-management") version "1.1.7"
    }
}

rootProject.name = "social-media-thoughts"

include(
    "authentication-service",
    "api-gateway",
    "eureka-service",
    "common",
)

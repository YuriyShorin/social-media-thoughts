rootProject.name = "social-media-thoughts"

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

include(
    "authentication-service",
    "api-gateway",
    "eureka-service",
    "common",
)

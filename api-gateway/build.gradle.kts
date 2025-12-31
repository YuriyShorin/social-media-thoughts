plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(project(":common"))

    implementation(libs.kotlin.reflect)

    implementation(libs.spring.boot.webflux)
    implementation(libs.spring.boot.actuator)

    implementation(platform(libs.spring.cloud.bom))
    implementation(libs.spring.cloud.eureka.client)
    implementation(libs.spring.cloud.gateway)

    implementation(libs.springdoc.webflux)

    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xannotation-default-target=param-property",
        )
    }
}

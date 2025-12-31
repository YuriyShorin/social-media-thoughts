plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    jacoco
}

dependencies {
    implementation(project(":common"))

    implementation(libs.kotlin.reflect)

    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.data.jpa)
    implementation(libs.spring.boot.validation)
    implementation(libs.spring.boot.security)
    implementation(libs.spring.boot.actuator)
    implementation(libs.spring.boot.liquibase)

    implementation(platform(libs.spring.cloud.bom))
    implementation(libs.spring.cloud.eureka.client)

    implementation(libs.springdoc.webmvc)

    implementation(libs.hibernate.core)
    implementation(libs.postgresql)

    implementation(libs.geoip)

    implementation(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.jackson)

    testImplementation(libs.spring.boot.test)
    testImplementation(libs.mockito.kotlin)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JacocoReport> {
    dependsOn("test")
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude(
                        "**/generated/**",
                        "ru/shorin/authenticationservice/config/**",
                        "ru/shorin/authenticationservice/dto/**",
                        "ru/shorin/authenticationservice/exception/**",
                        "ru/shorin/authenticationservice/mapper/**",
                        "ru/shorin/authenticationservice/model/**",
                        "ru/shorin/authenticationservice/AuthenticationServiceApplication.kt",
                    )
                }
            },
        ),
    )
}

tasks.withType<JacocoCoverageVerification> {
    dependsOn("jacocoTestReport")
    violationRules {
        rule {
            limit {
                minimum = 0.toBigDecimal()
            }
        }
    }
}

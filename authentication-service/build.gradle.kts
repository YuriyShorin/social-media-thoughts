plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("kapt")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    jacoco
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.1.0")
    }
}

dependencies {
    // common
    implementation(project(":common"))

    // kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // spring boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-liquibase")

    // spring cloud
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    // documentation
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.0")

    // databases
    implementation("org.hibernate:hibernate-core:7.1.10.Final")
    implementation("org.postgresql:postgresql:42.7.8")

    // jwt
    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")

    // ip
    implementation("com.maxmind.geoip2:geoip2:5.0.2")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:6.1.0")
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
                minimum = 0.25.toBigDecimal()
            }
        }
    }
}

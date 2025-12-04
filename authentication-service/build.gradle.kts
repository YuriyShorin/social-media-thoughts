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
        languageVersion = JavaLanguageVersion.of(21)
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

    // mapping
    implementation("org.mapstruct:mapstruct:1.6.3")
    kapt("org.mapstruct:mapstruct-processor:1.6.3")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

kapt {
    arguments {
        arg("mapstruct.defaultComponentModel", "spring")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
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
                        "ru/shorin/config/**",
                        "ru/shorin/dto/**",
                        "ru/shorin/exception/**",
                        "ru/shorin/mapper/**",
                        "ru/shorin/model/**",
                        "ru/shorin/Application",
                    )
                }
            },
        ),
    )
}

tasks.register<JacocoCoverageVerification>("jacocoCoverageVerification") {
    dependsOn("jacocoTestReport")

    violationRules {
        rule {
            limit {
                minimum = 0.80.toBigDecimal()
            }
        }
    }
}

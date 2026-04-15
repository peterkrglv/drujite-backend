val swaggerCodegenVersion = "1.0.9"
val exposedVersion: String by project
val h2Version: String by project
val kotlinVersion: String by project
val ktorVersion: String by project
val logbackVersion: String by project
val postgresVersion: String by project

plugins {
    kotlin("jvm") version "2.1.20"
    id("io.ktor.plugin") version "3.1.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
    id("org.jlleitschuh.gradle.ktlint") version "12.2.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
}

group = "ru.drujite"
version = "0.0.1"

application {
    mainClass = "ru.drujite.ApplicationKt"
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("io.ktor:ktor-server-swagger")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("com.h2database:h2:$h2Version")
    implementation("org.postgresql:postgresql:$postgresVersion")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-server-config-yaml")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("io.ktor:ktor-server-openapi:$ktorVersion")
    implementation("io.swagger.codegen.v3:swagger-codegen-generators:$swaggerCodegenVersion")
    implementation("io.ktor:ktor-server-cors-jvm:2.3.4")
    implementation("org.mindrot:jbcrypt:0.4")
}

tasks.withType<ProcessResources> {
    from("openapi") {
        into("openapi")
    }
}

ktlint {
    version.set("1.4.1")
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/config/detekt/detekt.yml")
}

tasks.check {
    dependsOn("ktlintCheck", "detekt")
}

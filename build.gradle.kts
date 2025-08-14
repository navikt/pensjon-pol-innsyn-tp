import org.jetbrains.kotlin.gradle.dsl.JvmTarget

group = "no.nav.pensjon"
version = "1"

val jacksonVersion = "2.19.0"

plugins {
    kotlin("jvm") version "2.1.21"
    kotlin("plugin.spring") version "2.2.10"
    id("org.springframework.boot") version "3.4.5"
    id("io.spring.dependency-management") version "1.1.7"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", jacksonVersion)
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion)
    implementation("io.micrometer", "micrometer-registry-prometheus", "1.15.0")
    implementation("net.logstash.logback", "logstash-logback-encoder", "8.1")
    implementation("no.nav.common", "token-client", "2.2023.01.10_13.49-81ddc732df3a")
    implementation("org.apache.poi", "poi-ooxml", "5.3.0")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-webflux")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-validation")
    implementation("org.springframework.boot", "spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot", "spring-boot-starter-thymeleaf")
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.platform", "junit-platform-launcher")
    testImplementation("org.wiremock", "wiremock-jetty12", "3.13.0")
    testImplementation("com.ninja-squad", "springmockk", "4.0.2")
    testImplementation("org.springframework.boot", "spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    testImplementation("org.springframework.security", "spring-security-test")
}

tasks {
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }
    test {
        useJUnitPlatform()
    }
}

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "no.nav.pensjon"
version = "1"

val jacksonVersion = "2.16.0"

plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.fasterxml.jackson.datatype", "jackson-datatype-jsr310", jacksonVersion)
    implementation("com.fasterxml.jackson.module", "jackson-module-kotlin", jacksonVersion)
    implementation("io.micrometer", "micrometer-registry-prometheus", "1.12.0")
    implementation("net.logstash.logback", "logstash-logback-encoder", "7.4")
    implementation("no.nav.common", "token-client", "2.2022.09.02_11.04-2530dd139a0a")
    implementation("org.apache.poi", "poi-ooxml", "5.2.5")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-webflux")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-validation")
    implementation("org.springframework.boot", "spring-boot-starter-oauth2-client")
    implementation("org.springframework.boot", "spring-boot-starter-thymeleaf")
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.platform", "junit-platform-launcher")
    testImplementation("com.github.tomakehurst", "wiremock", "3.0.1")
    testImplementation("com.ninja-squad", "springmockk", "4.0.2")
    testImplementation("org.springframework.boot", "spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    testImplementation("org.springframework.security", "spring-security-test")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "20"
    }
    test {
        useJUnitPlatform()
    }
}

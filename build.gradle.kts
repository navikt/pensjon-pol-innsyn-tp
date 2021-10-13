import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "no.nav.pensjon"
version = "1"

plugins {
    kotlin("jvm") version "1.6.0-RC"
    kotlin("plugin.spring") version "1.6.0-RC"
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("io.micrometer", "micrometer-registry-prometheus", "1.7.4")
    implementation("net.logstash.logback", "logstash-logback-encoder", "6.6")
    implementation("no.nav.security", "token-validation-spring", "1.3.0")
    implementation("org.apache.poi", "poi-ooxml", "5.0.0")
    implementation("org.springframework.boot", "spring-boot-starter-web")
    implementation("org.springframework.boot", "spring-boot-starter-webflux")
    implementation("org.springframework.boot", "spring-boot-starter-actuator")
    implementation("org.springframework.boot", "spring-boot-starter-validation")
    testImplementation(kotlin("test-junit5"))
    testImplementation("com.ninja-squad", "springmockk", "3.0.1")
    testImplementation("no.nav.security", "token-validation-test-support", "1.3.0")
    testImplementation("org.springframework.boot", "spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        exclude(module = "mockito-core")
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "16"
    }
    test {
        useJUnitPlatform()
    }
}

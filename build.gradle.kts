group = "no.nav.pensjon"
version = "1"

val commonsLang3Version = "3.21.0"

plugins {
    kotlin("jvm") version "2.4.21"
    kotlin("plugin.spring") version "2.4.21"
    id("org.springframework.boot") version "4.1.2"
    id("io.spring.dependency-management") version "1.1.7"
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("com.azure.spring:spring-cloud-azure-starter-active-directory:7.4.0")
    implementation("io.micrometer:micrometer-registry-prometheus:1.17.2")
    implementation("net.logstash.logback:logstash-logback-encoder:9.0")
    implementation("org.apache.poi:poi-ooxml:5.5.1")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security-oauth2-client")
    implementation("org.springframework.boot:spring-boot-starter-security-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.apache.commons:commons-lang3:$commonsLang3Version")
    testImplementation(kotlin("test-junit5"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.wiremock.integrations:wiremock-spring-boot:4.4.3")
    testImplementation("com.ninja-squad:springmockk:5.0.1")
    testImplementation("org.springframework.boot:spring-boot-starter-security-oauth2-client-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-oauth2-resource-server-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test") {
        exclude(module = "mockito-core")
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
    configurations.all {
        resolutionStrategy.eachDependency {
            when (requested.group) {
                "com.azure" if (requested.name == "azure-core") -> useVersion("1.60.0")
                "com.fasterxml.jackson.core" if (requested.name == "jackson-core" || requested.name == "jackson-databind")
                    -> useVersion("2.22.4")
                "com.fasterxml.jackson.core" if requested.name == "jackson-annotations" -> useVersion("2.22")
                "tools.jackson.core" -> useVersion("3.2.4")
                "ch.qos.logback" -> useVersion("1.6.5")
                "org.apache.commons" if requested.name == "commons-lang" -> useVersion(commonsLang3Version)
                "org.apache.qpid" -> useVersion("0.35.0")
                "org.apache.tomcat.embed" -> useVersion("11.0.27")
                "org.ow2.asm" -> useVersion("9.11.1")
                "io.netty" if (requested.version == "4.2.17.Final"
                        || requested.name.startsWith("netty-buffer")
                        || requested.name.startsWith("netty-codec")
                        || requested.name.startsWith("netty-common")
                        || requested.name.startsWith("netty-handler")
                        || requested.name.startsWith("netty-resolver")
                        || requested.name.startsWith("netty-transport")
                ) -> useVersion("4.2.19.Final")
            }
        }
    }
}

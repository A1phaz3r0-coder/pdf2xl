import jdk.jfr.internal.JVM.exclude

plugins {
    java
    id("org.springframework.boot") version "4.0.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "pdfToXML"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-hateoas")
    implementation("org.springframework.boot:spring-boot-starter-jersey")
    // Source: https://mvnrepository.com/artifact/technology.tabula/tabula
    implementation("technology.tabula:tabula:1.0.5")
    implementation("org.apache.commons:commons-lang3:3.20.0")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-data-rest-test")
    testImplementation("org.springframework.boot:spring-boot-starter-hateoas-test")
    testImplementation("org.springframework.boot:spring-boot-starter-jersey-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

configurations.all {
    exclude(group = "org.slf4j", module = "slf4j-log4j12")
    exclude(group = "org.slf4j", module = "slf4j-jdk14")
    exclude(group = "org.slf4j", module = "slf4j-simple")
    // Exclude slf4j-nop if present and causing conflict
    exclude(group = "org.slf4j", module = "slf4j-nop")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

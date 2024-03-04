buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:10.4.1")
    }
}

plugins {
    java
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
    jacoco
    id("org.flywaydb.flyway") version "10.4.1"
}

group = "br.com.financialtoolapi"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

tasks.named<Jar>("jar") {
    enabled = false
}

jacoco {
    toolVersion = "0.8.9"
}
tasks.jacocoTestReport {
    reports {
        xml.required.set(false)
        csv.required.set(false)
    }

    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) {
            setExcludes(listOf())
        }
    }))
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
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("io.vavr:vavr:0.10.4")

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation("org.springframework.boot:spring-boot-starter-actuator:3.1.5")
    implementation("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    implementation("org.postgresql:postgresql")
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("com.h2database:h2:2.2.224")
    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation("org.apache.httpcomponents.client5:httpclient5")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

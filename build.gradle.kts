import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.7.22"
    val springbootVersion = "2.7.8"
    id("org.springframework.boot") version springbootVersion
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

    id("org.jlleitschuh.gradle.ktlint") version "11.1.0"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "com.idd"
version = "0.3.0"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

val asciidoctorExtensions: Configuration by configurations.creating

dependencies {
    implementation("org.testng:testng:7.1.0")
    val jjwtVersion = "0.11.2"
    val kotestVersion = "5.5.5"

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")

    runtimeOnly("com.h2database:h2:2.1.214")

    compileOnly("io.jsonwebtoken:jjwt-api:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jjwtVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:$jjwtVersion")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")

    runtimeOnly("com.oracle.database.jdbc:ojdbc11:21.9.0.0")

    implementation("org.apache.httpcomponents:httpclient:4.5.13")

    testImplementation("io.rest-assured:rest-assured")

    asciidoctorExtensions("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.restdocs:spring-restdocs-restassured")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    val snippetsDir by extra { file("build/generated-snippets") }

    withType<Test>().configureEach {
        outputs.dir(snippetsDir)
        useJUnitPlatform()
    }

    ktlint {
        verbose.set(true)
    }

    asciidoctor {
        doFirst {
            delete("build/docs/asciidoc/")
        }
        dependsOn(test)
        configurations(asciidoctorExtensions.name)
        baseDirFollowsSourceFile()
        inputs.dir(snippetsDir)
        doLast {
            copy {
                from(file("build/docs/asciidoc/index.html"))
                into(file("src/main/resources/static/docs"))
            }
        }
    }

    bootJar {
        dependsOn("asciidoctor")
    }
}

plugins {
    kotlin("multiplatform") apply false
    kotlin("jvm") apply false
    kotlin("js") apply false
    kotlin("kapt") apply false
}

group = "ru.otus.otuskotlin.marketplace-rv"
version = "0.0.1"

subprojects {
    group = rootProject.group
    version = rootProject.version

    repositories {
        jcenter()
        mavenCentral()
        maven { url = uri("https://dl.bintray.com/kotlin/kotlin-js-wrappers") }
        maven { url = uri("https://jitpack.io") }
    }
    plugins.withId("org.jetbrains.kotlin.jvm") {
        tasks {
            withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
                kotlinOptions {
                    freeCompilerArgs = listOf("-Xjsr305=strict")
                    jvmTarget = "11"
                }
            }
        }
    }
}
plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))

    implementation(project(":ok-marketplace-rv-be-common"))
    implementation(project(":ok-marketplace-rv-mp-transport"))
}

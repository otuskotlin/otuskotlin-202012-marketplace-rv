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
    implementation("junit:junit:4.12")
    testImplementation(kotlin("test"))
}

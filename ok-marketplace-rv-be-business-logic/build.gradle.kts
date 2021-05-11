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
    implementation(project(":ok-marketplace-rv-be-common"))
    implementation(project(":ok-marketplace-rv-mp-pipelines"))
    implementation(project(":ok-marketplace-rv-mp-pipelines-validation"))
    implementation(project(":ok-marketplace-rv-mp-common"))

    implementation("junit:junit:4.12")
    testImplementation(kotlin("test"))
}

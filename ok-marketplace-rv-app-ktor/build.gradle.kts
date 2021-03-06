val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project
val ktorRabbitmqFeature: String by project
val rabbitmqVersion: String by project
val testContainersVersion: String by project

plugins {
    application
    kotlin("jvm")
    id("com.bmuschko.docker-java-application")
}

group = rootProject.group
version = rootProject.version

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

docker {
    javaApplication {
        baseImage.set("adoptopenjdk/openjdk11:alpine-jre")
        maintainer.set("(c) Otus")
        ports.set(listOf(8080))
        val imageName = project.name
        images.set(listOf(
            "$imageName:${project.version}",
            "$imageName:latest"
        ))
        jvmArgs.set(listOf("-Xms256m", "-Xmx512m"))
    }
}

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {

    implementation(project(":ok-marketplace-rv-be-common"))
    implementation(project(":ok-marketplace-rv-be-mappers-mp"))
    implementation(project(":ok-marketplace-rv-be-business-logic"))
    implementation(project(":ok-marketplace-rv-mp-transport"))
    implementation(project(":ok-marketplace-rv-be-repository-inmemory"))
    implementation(project(":ok-marketplace-rv-be-repository-cassandra"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

    implementation("com.github.JUtupe:ktor-rabbitmq:$ktorRabbitmqFeature")
    implementation("com.rabbitmq:amqp-client:$rabbitmqVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.testcontainers:rabbitmq:$testContainersVersion")
}
kotlin.sourceSets["main"].kotlin.srcDirs("src")
kotlin.sourceSets["test"].kotlin.srcDirs("test")
sourceSets["main"].resources.srcDirs("resources")
sourceSets["test"].resources.srcDirs("testresources")
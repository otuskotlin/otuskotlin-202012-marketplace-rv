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
}

group = rootProject.group
version = rootProject.version

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
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

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-serialization:$ktorVersion")
    implementation("io.ktor:ktor-websockets:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")

//    implementation("com.github.JUtupe:ktor-rabbitmq:$ktorRabbitmqFeature")
//    implementation("com.rabbitmq:amqp-client:$rabbitmqVersion")

    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
}


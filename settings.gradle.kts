pluginManagement {
    val kotlinVersion: String by settings
    val bmuschkoVersion: String by settings

    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        kotlin("js") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion

        id("com.bmuschko.docker-java-application") version bmuschkoVersion
    }
}
rootProject.name = "otuskotlin-202012-marketplace-rv"
//app
include("ok-marketplace-rv-app-ktor")
//backend
include("ok-marketplace-rv-be-common")
include("ok-marketplace-rv-be-mappers-mp")
include("ok-marketplace-rv-be-business-logic")
//multiplatform
include("ok-marketplace-rv-mp-common")
include("ok-marketplace-rv-mp-transport")
include("ok-marketplace-rv-mp-pipelines")
include("ok-marketplace-rv-mp-pipelines-validation")

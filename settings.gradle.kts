pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("multiplatform") version kotlinVersion apply false
        kotlin("jvm") version kotlinVersion apply false
        kotlin("js") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
    }
}
rootProject.name = "otuskotlin-202012-marketplace-rv"
//app
include("ok-marketplace-rv-app-ktor")
//backend
include("ok-marketplace-rv-be-common")
include("ok-marketplace-rv-be-mappers-mp")
//multiplatform
include("ok-marketplace-rv-mp-common")
include("ok-marketplace-rv-mp-transport")



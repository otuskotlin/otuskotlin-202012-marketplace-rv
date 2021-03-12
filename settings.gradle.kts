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
include("ok-marketplace-rv-common-be")
include("ok-marketplace-rv-common-mp")
include("ok-marketplace-transport-mp")
include("ok-marketplace-rv-be-mappers-mp")
include("ok-marketplace-rv-app-ktor")

pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        kotlin("js") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false
    }
}
rootProject.name = "otuskotlin-202012-marketplace-rv"
include("ok-marketplace-rv-common-be")
include("ok-marketplace-rv-common-mp")
include("ok-marketplace-transport-mp")
include("ok-marketplace-rv-be-mappers-mp")

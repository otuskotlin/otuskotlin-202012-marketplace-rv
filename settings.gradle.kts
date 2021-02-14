pluginManagement {
    val kotlinVersion: String by settings
    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("jvm") version kotlinVersion
        kotlin("js") version kotlinVersion
    }
}
rootProject.name = "otuskotlin-202012-marketplace-rv"
include("ok-marketplace-rv-common-be")
include("ok-marketplace-rv-common-mp")

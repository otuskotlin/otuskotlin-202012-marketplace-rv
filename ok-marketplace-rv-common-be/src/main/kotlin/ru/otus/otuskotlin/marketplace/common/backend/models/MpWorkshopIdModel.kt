package ru.otus.otuskotlin.marketplace.common.backend.models

data class MpWorkshopIdModel(
    override val id: String
) : IMpThingIdModel {
    companion object {
        val NONE = MpWorkshopIdModel("")
    }
}


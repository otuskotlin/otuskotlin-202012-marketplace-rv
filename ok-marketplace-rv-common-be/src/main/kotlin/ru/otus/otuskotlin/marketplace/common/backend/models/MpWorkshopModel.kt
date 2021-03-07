package ru.otus.otuskotlin.marketplace.common.backend.models

data class MpWorkshopIdModel(
    override val id: String
) : IMpThingIdModel {
    companion object {
        val NONE = MpWorkshopIdModel("")
    }
}

data class MpWorkshopModel(
    override val id: MpWorkshopIdModel = MpWorkshopIdModel.NONE,
    override val title: String = "",
    override val description: String = "",
    override val tagIds: MutableSet<String> = mutableSetOf()
) : IMpThingModel {
    companion object {
        val NONE = MpWorkshopModel()
    }
}

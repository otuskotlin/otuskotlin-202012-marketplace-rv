package ru.otus.otuskotlin.marketplace.common.backend.models

data class MpWorkshopModel(
    override val id: MpWorkshopIdModel = MpWorkshopIdModel.NONE,
    override val title: String,
    override val description: String,
    override val tagIds: MutableSet<String>
) : IMpThingModel {
    companion object {
        val NONE = MpWorkshopModel
    }
}

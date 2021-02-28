package ru.otus.otuskotlin.marketplace.common.backend.models

data class MpArtModel(
    override val id: MpArtIdModel = MpArtIdModel.NONE,
    override val title: String,
    override val description: String,
    override val tagIds: MutableSet<String>
) : IMpThingModel {
    companion object {
        val NONE = MpArtModel
    }
}

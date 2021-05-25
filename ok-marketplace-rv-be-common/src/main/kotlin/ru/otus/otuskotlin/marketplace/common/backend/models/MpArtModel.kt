package ru.otus.otuskotlin.marketplace.common.backend.models


inline class MpArtIdModel(
    override val id: String
) : IMpThingIdModel<String> {
    companion object {
        val NONE = MpArtIdModel("")
    }
}

data class MpArtModel(
    override val id: MpArtIdModel = MpArtIdModel.NONE,
    override val title: String = "",
    override val description: String = "",
    override val tagIds: MutableSet<String> = mutableSetOf(),
) : IMpThingModel {
    companion object {
        val NONE = MpArtModel ()
    }
}

data class MpArtFilterModel(
    val text: String = "",
    val includeDescription: Boolean = false,
    val sortBy: MpSortModel = MpSortModel.NONE,
    val offset: Int = Int.MIN_VALUE,
    val count: Int = Int.MIN_VALUE,
) {
    companion object {
        val NONE = MpArtFilterModel()
    }
}

package ru.otus.otuskotlin.marketplace.common.backend.models

import java.util.*

data class MpWorkshopIdModel(
    override val id: String
) : IMpThingIdModel {
    companion object {
        val NONE = MpWorkshopIdModel("")
    }

    fun asString() = id
    fun asUUID(): UUID = UUID.fromString(id)
}

data class MpWorkshopModel(
    override val id: MpWorkshopIdModel = MpWorkshopIdModel.NONE,
    override val title: String = "",
    override val description: String = "",
    override val tagIds: MutableSet<String> = mutableSetOf(),
    val arts: MutableSet<MpArtIdModel> = mutableSetOf(),
) : IMpThingModel {
    companion object {
        val NONE = MpWorkshopModel()
    }

}

data class MpWorkshopFilterModel(
    val text: String = "",
    val includeDescription: Boolean = false,
    val sortBy: MpSortModel = MpSortModel.NONE,
    val offset: Int = Int.MIN_VALUE,
    val count: Int = Int.MIN_VALUE,
) {
    companion object {
        val NONE = MpWorkshopFilterModel()
    }
}

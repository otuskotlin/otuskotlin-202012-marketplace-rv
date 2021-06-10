package ru.otus.otuskotlin.marketplace.backend.repository.inmemory.workshops

import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopModel

data class WorkshopInMemoryDto(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val tagIds: Set<String>? = null,
) {
    fun toModel() = MpWorkshopModel(
        id = id?.let { MpWorkshopIdModel(it) }?: MpWorkshopIdModel.NONE,
        title = title?: "",
        description = description?: "",
        tagIds = tagIds?.toMutableSet()?: mutableSetOf(),
    )

    companion object {

        fun of(model: MpWorkshopModel) = of(model, model.id.id)

        fun of(model: MpWorkshopModel, id: String) = WorkshopInMemoryDto(
            id = id.takeIf { it.isNotBlank() },
            title = model.title.takeIf { it.isNotBlank() },
            description = model.description.takeIf { it.isNotBlank() },
            tagIds = model.tagIds.takeIf { it.isNotEmpty() },
        )
    }
}
package ru.otus.otuskotlin.marketplace.backend.repository.inmemory.arts

import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel

data class ArtInMemoryDto(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val tagIds: Set<String>? = null,
) {
    fun toModel() = MpArtModel(
        id = id?.let { MpArtIdModel(it) }?: MpArtIdModel.NONE,
        title = title?: "",
        description = description?: "",
        tagIds = tagIds?.toMutableSet()?: mutableSetOf(),
    )

    companion object {

        fun of(model: MpArtModel) = of(model, model.id.id)

        fun of(model: MpArtModel, id: String) = ArtInMemoryDto(
            id = id.takeIf { it.isNotBlank() },
            title = model.title.takeIf { it.isNotBlank() },
            description = model.description.takeIf { it.isNotBlank() },
            tagIds = model.tagIds.takeIf { it.isNotEmpty() },
        )
    }
}
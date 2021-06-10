package ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.arts

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.intermediateTables.ArtsTagsTable
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.tags.TagDto
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import java.util.*

class ArtDto(id: EntityID<UUID>): UUIDEntity(id) {

    var title by ArtsTable.title
    var description by ArtsTable.description
    var tagIds by TagDto via ArtsTagsTable

    fun toModel() = MpArtModel(
        id = MpArtIdModel(id.value.toString()),
        title = title,
        description = description,
    )

    fun of(model: MpArtModel) {
        title = model.title
        description = model.description
    }

    companion object : UUIDEntityClass<ArtDto>(ArtsTable)
}
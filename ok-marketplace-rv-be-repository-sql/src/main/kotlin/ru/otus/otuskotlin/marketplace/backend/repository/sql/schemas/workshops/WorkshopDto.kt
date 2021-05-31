package ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.workshops

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.arts.ArtDto
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.intermediateTables.WorkshopsArtsTable
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.intermediateTables.WorkshopsTagsTable
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.tags.TagDto
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopModel
import java.util.*

class WorkshopDto(id: EntityID<UUID>): UUIDEntity(id) {

    var title by WorkshopsTable.title
    var description by WorkshopsTable.description
    var arts by ArtDto via WorkshopsArtsTable
    var tagIds by TagDto via WorkshopsTagsTable

    fun toModel() = MpWorkshopModel(
        id = MpWorkshopIdModel(id.value.toString()),
        title = title,
        description = description,
    )

    fun of(model: MpWorkshopModel) {
        title = model.title
        description = model.description
    }

    companion object : UUIDEntityClass<WorkshopDto>(WorkshopsTable)
}
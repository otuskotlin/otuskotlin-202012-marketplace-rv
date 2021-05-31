package ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.tags

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import java.util.*

class TagDto(id: EntityID<UUID>): UUIDEntity(id)  {
    var tag by TagsTable.tag
    var tagId by TagsTable.id

    companion object : UUIDEntityClass<TagDto>(TagsTable)
}
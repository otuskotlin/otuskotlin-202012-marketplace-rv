package ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.intermediateTables

import org.jetbrains.exposed.sql.Table
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.arts.ArtsTable
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.tags.TagsTable

object ArtsTagsTable : Table() {
    val art = reference("art", ArtsTable)
    val tagId = reference("tagId", TagsTable)
    override val primaryKey = PrimaryKey(art, tagId, name = "PK_arts_tags")
}
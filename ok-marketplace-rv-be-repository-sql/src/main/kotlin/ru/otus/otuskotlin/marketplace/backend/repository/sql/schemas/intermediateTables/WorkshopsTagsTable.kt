package ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.intermediateTables

import org.jetbrains.exposed.sql.Table
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.arts.ArtsTable
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.tags.TagsTable

object WorkshopsTagsTable : Table() {
    val workshop = reference("workshop", ArtsTable)
    val tagId = reference("tagId", TagsTable)
    override val primaryKey = PrimaryKey(workshop, tagId, name = "PK_workshops_tags")
}
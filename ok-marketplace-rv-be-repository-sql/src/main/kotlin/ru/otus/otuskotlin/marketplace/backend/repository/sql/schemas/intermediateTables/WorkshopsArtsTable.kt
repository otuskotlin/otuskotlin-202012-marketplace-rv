package ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.intermediateTables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Table
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.arts.ArtsTable
import ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.tags.TagsTable

object WorkshopsArtsTable : Table() {
    val art = reference("art", ArtsTable)
    val workshop = reference("workshop", TagsTable)
    override val primaryKey = PrimaryKey(art, workshop, name = "PK_workshops_arts")
}
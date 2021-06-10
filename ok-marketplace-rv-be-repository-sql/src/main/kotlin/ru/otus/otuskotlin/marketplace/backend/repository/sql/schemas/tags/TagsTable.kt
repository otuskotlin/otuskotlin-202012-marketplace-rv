package ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.tags

import org.jetbrains.exposed.dao.id.UUIDTable

object TagsTable: UUIDTable("tags") {
    val tag = varchar("tag", 256)
}
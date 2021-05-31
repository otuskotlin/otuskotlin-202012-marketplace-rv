package ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.arts

import org.jetbrains.exposed.dao.id.UUIDTable

object ArtsTable: UUIDTable("arts") {

    val title = varchar("title", 256)
    val description = text("description")
}
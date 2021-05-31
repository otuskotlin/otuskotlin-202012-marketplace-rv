package ru.otus.otuskotlin.marketplace.backend.repository.sql.schemas.workshops

import org.jetbrains.exposed.dao.id.UUIDTable

object WorkshopsTable: UUIDTable("workshops") {

    val title = varchar("title", 256)
    val description = text("description")
}
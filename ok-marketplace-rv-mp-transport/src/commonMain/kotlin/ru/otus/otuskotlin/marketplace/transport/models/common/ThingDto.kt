package ru.otus.otuskotlin.marketplace.transport.models.common

interface IMpCreateThingDto {
    val title: String?
    val description: String?
    val tagIds: Set<String>?
}
interface IMpUpdateThingDto : IMpCreateThingDto {
    val id: String?
}

interface IMpThingDto : IMpUpdateThingDto

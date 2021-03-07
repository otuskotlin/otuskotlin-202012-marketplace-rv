package ru.otus.otuskotlin.marketplace.common.backend.models

interface IMpThingIdModel {
    val id: String
}

interface IMpThingModel {
    val id: IMpThingIdModel
    val title: String
    val description: String
    val tagIds: MutableSet<String>
}
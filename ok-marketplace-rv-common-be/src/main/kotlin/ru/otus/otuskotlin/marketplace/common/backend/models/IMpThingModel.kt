package ru.otus.otuskotlin.marketplace.common.backend.models

interface IMpThingModel {
    val id: IMpThingIdModel
    val title: String
    val description: String
    val tagIds: MutableSet<String>
}
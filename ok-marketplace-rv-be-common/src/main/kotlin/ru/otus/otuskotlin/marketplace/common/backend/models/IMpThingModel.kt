package ru.otus.otuskotlin.marketplace.common.backend.models

interface IMpThingIdModel<E> {
    val id: E
}

interface IMpThingModel {
    val id: IMpThingIdModel<String>
    val title: String
    val description: String
    val tagIds: MutableSet<String>
}
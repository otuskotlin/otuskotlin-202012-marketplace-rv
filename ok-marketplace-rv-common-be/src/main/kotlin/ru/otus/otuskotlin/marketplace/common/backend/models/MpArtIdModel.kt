package ru.otus.otuskotlin.marketplace.common.backend.models

inline class MpArtIdModel(
    override val id: String
) : IMpThingIdModel {
    companion object {
        val NONE = MpArtIdModel("")
    }
}

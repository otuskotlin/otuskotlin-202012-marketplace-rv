package ru.otus.otuskotlin.marketplace.transport.models.common

import kotlinx.serialization.Serializable

@Serializable
enum class MpStatusDTO {
    SUCCESS,
    BAD_REQUEST,
    NOT_FOUND
}

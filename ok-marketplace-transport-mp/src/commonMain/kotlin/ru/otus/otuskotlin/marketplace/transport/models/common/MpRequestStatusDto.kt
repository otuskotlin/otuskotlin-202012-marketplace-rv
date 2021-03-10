package ru.otus.otuskotlin.marketplace.transport.models.common

import kotlinx.serialization.Serializable

@Serializable
enum class MpRequestStatusDto {
    SUCCESS,
    BAD_REQUEST,
    NOT_FOUND
}

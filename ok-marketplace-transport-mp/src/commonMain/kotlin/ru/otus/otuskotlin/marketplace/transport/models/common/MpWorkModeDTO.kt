package ru.otus.otuskotlin.marketplace.transport.models.common

import kotlinx.serialization.Serializable

@Serializable
enum class MpWorkModeDTO {
    PROD,
    STUB,
}

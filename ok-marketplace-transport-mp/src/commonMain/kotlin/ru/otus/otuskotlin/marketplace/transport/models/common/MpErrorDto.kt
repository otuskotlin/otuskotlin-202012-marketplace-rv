package ru.otus.otuskotlin.marketplace.transport.models.common

import kotlinx.serialization.Serializable

@Serializable
data class MpErrorDto(
    val code: String?,
    val message: String?,
    val field: String?,
    val level: ErrorLevelDto?,
) {
    @Serializable
    enum class ErrorLevelDto {
        INFO,
        WARNING,
        ERROR,
    }
}

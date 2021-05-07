package ru.otus.otuskotlin.marketplace.transport.models.common

import kotlinx.serialization.Serializable

@Serializable
data class MpErrorDto(
    val code: String? = null,
    val message: String? = null,
    val field: String? = null,
    val level: ErrorLevelDto? = null,
) {
    @Serializable
    enum class ErrorLevelDto {
        INFO,
        WARNING,
        ERROR,
    }
}

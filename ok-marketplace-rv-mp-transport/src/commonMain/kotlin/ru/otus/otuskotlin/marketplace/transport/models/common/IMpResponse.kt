package ru.otus.otuskotlin.marketplace.transport.models.common

interface IMpResponse {
    val responseId: String?
    val onRequestId: String?
    val debug: IMpDebug?
    val responseStatus: MpResponseStatusDto?
    val errors: List<MpErrorDto>?
}
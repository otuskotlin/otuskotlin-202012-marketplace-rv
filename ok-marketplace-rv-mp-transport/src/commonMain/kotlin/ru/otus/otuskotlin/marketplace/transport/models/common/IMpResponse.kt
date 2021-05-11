package ru.otus.otuskotlin.marketplace.transport.models.common

interface IMpResponse {
    val responseId: String?
    val onRequest: String?
    val debug: IMpDebug?
    val responseStatus: MpResponseStatusDto?
    val errors: List<MpErrorDto>?
    val status: ResponseStatusDto?
    val endTime: String?

}
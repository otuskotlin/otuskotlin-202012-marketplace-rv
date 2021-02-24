package ru.otus.otuskotlin.marketplace.transport.models.common

interface IMpResponse {
    val responseId: String?
    val onRequestId: String?
    val debug: IMpDebug?
    val status: MpStatusDTO?
    val errors: List<MpErrorDTO>?
}
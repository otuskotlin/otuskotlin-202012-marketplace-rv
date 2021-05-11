package ru.otus.otuskotlin.marketplace.transport.models.common

interface IMpRequest {
    val requestId: String?
    val onRequestResponseId: String?
    val timeStart: String?
    val debug: IMpDebug?
}
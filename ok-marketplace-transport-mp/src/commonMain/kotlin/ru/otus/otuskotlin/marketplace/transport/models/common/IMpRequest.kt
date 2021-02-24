package ru.otus.otuskotlin.marketplace.transport.models.common

interface IMpRequest {
    val requestId: String?
    val onRequestResponceId: String?
    val timeStart: String?
    val debug: IMpDebug?
}
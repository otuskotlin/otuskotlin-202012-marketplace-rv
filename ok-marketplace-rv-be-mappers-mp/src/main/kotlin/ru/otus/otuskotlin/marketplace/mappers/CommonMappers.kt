package ru.otus.otuskotlin.marketplace.mappers

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContextStatus
import ru.otus.otuskotlin.marketplace.common.backend.models.IMpError
import ru.otus.otuskotlin.marketplace.mappers.exceptions.WrongMpBeContextStatus
import ru.otus.otuskotlin.marketplace.transport.models.common.IMpRequest
import ru.otus.otuskotlin.marketplace.transport.models.common.MpErrorDto
import ru.otus.otuskotlin.marketplace.transport.models.common.ResponseStatusDto

fun IMpError.toTransport() = MpErrorDto(
    message = message
)

fun <T: IMpRequest> MpBeContext.setRequest(request: T, block: MpBeContext.() -> Unit) = apply {
    onRequest = request.requestId ?: ""
    block()
}

fun MpBeContextStatus.toTransport(): ResponseStatusDto = when(this) {
    MpBeContextStatus.NONE -> throw WrongMpBeContextStatus(this)
    MpBeContextStatus.RUNNING -> throw WrongMpBeContextStatus(this)
    MpBeContextStatus.FINISHING -> ResponseStatusDto.SUCCESS
    MpBeContextStatus.FAILING -> throw WrongMpBeContextStatus(this)
    MpBeContextStatus.SUCCESS -> ResponseStatusDto.SUCCESS
    MpBeContextStatus.ERROR -> ResponseStatusDto.BAD_REQUEST
}


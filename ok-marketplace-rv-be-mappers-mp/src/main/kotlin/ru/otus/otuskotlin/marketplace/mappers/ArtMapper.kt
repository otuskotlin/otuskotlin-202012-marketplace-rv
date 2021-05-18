package ru.otus.otuskotlin.marketplace.mappers

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtFilterModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpStubCase
import ru.otus.otuskotlin.marketplace.transport.models.arts.*
import java.time.Instant

fun MpBeContext.setArtCreateQuery(request: MpRequestArtCreate) {
    requestArt = MpArtModel (
        title = request.createData?.title?: "",
        description = request.createData?.description?: "",
        tagIds = request.createData?.tagIds?.toMutableSet()?: mutableSetOf(),
        )
    stubCase = when (request.debug?.stubCase) {
        MpRequestArtCreate.StubCase.SUCCESS -> MpStubCase.ART_READ_SUCCESS
        else -> MpStubCase.NONE
    }
}

fun MpBeContext.setArtReadQuery(request: MpRequestArtRead) {
    this.requestArtId = request.artId?.let { MpArtIdModel(it) } ?: MpArtIdModel.NONE
    stubCase = when (request.debug?.stubCase) {
        MpRequestArtRead.StubCase.SUCCESS -> MpStubCase.ART_READ_SUCCESS
        else -> MpStubCase.NONE
    }
}

fun MpBeContext.setArtUpdateQuery(request: MpRequestArtUpdate) {
    requestArt = MpArtModel (
        id = request.updateData?.id?.let {MpArtIdModel(it)} ?: MpArtIdModel.NONE,
        title = request.updateData?.title?: "",
        description = request.updateData?.description?: "",
        tagIds = request.updateData?.tagIds?.toMutableSet()?: mutableSetOf(),
    )
    stubCase = when (request.debug?.stubCase) {
        MpRequestArtUpdate.StubCase.SUCCESS -> MpStubCase.ART_READ_SUCCESS
        else -> MpStubCase.NONE
    }
}

fun MpBeContext.setArtDeleteQuery(request: MpRequestArtDelete) {
    this.requestArtId = request.artId?.let { MpArtIdModel(it) } ?: MpArtIdModel.NONE
    stubCase = when (request.debug?.stubCase) {
        MpRequestArtDelete.StubCase.SUCCESS -> MpStubCase.ART_READ_SUCCESS
        else -> MpStubCase.NONE
    }
}

fun MpBeContext.setArtListQuery(request: MpRequestArtList) {
    this.requestArtFilter = request.filterData?.let {
        MpArtFilterModel(
            text = it.text?: ""
        )
    }?: MpArtFilterModel.NONE
    stubCase = when (request.debug?.stubCase) {
        MpRequestArtList.StubCase.SUCCESS -> MpStubCase.ART_READ_SUCCESS
        else -> MpStubCase.NONE
    }
}

fun MpBeContext.respondArtCreate() = MpResponseArtCreate(
    art = responseArt.takeIf { it != MpArtModel.NONE }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    status = status.toTransport(),
    responseId = responseId,
    onRequest = onRequest,
    endTime = Instant.now().toString()
)

fun MpBeContext.respondArtRead() = MpResponseArtRead(
    art = responseArt.takeIf { it != MpArtModel.NONE }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    status = status.toTransport(),
    responseId = responseId,
    onRequest = onRequest,
    endTime = Instant.now().toString()
)

fun MpBeContext.respondArtUpdate() = MpResponseArtUpdate(
    art = responseArt.takeIf { it != MpArtModel.NONE }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    status = status.toTransport(),
    responseId = responseId,
    onRequest = onRequest,
    endTime = Instant.now().toString()
)

fun MpBeContext.respondArtDelete() = MpResponseArtDelete(
    art = responseArt.takeIf { it != MpArtModel.NONE }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    status = status.toTransport(),
    responseId = responseId,
    onRequest = onRequest,
    endTime = Instant.now().toString()
)

fun MpBeContext.respondArtList() = MpResponseArtList(
    arts = responseArts.takeIf { it.isNotEmpty() }?.filter { it != MpArtModel.NONE }
        ?.map { it.toTransport() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    status = status.toTransport(),
    responseId = responseId,
    onRequest = onRequest,
    endTime = Instant.now().toString()
)

internal fun MpArtModel.toTransport() = MpArtDto(
    id = id.id.takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    tagIds = tagIds.takeIf { it.isNotEmpty() },
)
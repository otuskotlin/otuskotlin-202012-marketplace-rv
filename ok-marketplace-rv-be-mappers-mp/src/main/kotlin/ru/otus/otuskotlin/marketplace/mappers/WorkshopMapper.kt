package ru.otus.otuskotlin.marketplace.mappers

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.models.*
import ru.otus.otuskotlin.marketplace.transport.models.arts.*
import ru.otus.otuskotlin.marketplace.transport.models.workshops.*
import java.time.Instant

fun MpBeContext.setWorkshopCreateQuery(request: MpRequestWorkshopCreate) = setRequest(request) {
    requestWorkshop = MpWorkshopModel (
        title = request.createData?.title?: "",
        description = request.createData?.description?: "",
        tagIds = request.createData?.tagIds?.toMutableSet()?: mutableSetOf(),
    )
    stubCase = when (request.debug?.stubCase) {
        MpRequestWorkshopCreate.StubCase.SUCCESS -> MpStubCase.ART_READ_SUCCESS
        else -> MpStubCase.NONE
    }
    workMode = request.debug?.mode.toModel()
}

fun MpBeContext.setWorkshopReadQuery(request: MpRequestWorkshopRead) = setRequest(request) {
    this.requestWorkshopId = request.workshopId?.let { MpWorkshopIdModel(it) } ?: MpWorkshopIdModel.NONE
    stubCase = when (request.debug?.stubCase) {
        MpRequestWorkshopRead.StubCase.SUCCESS -> MpStubCase.ART_READ_SUCCESS
        else -> MpStubCase.NONE
    }
    workMode = request.debug?.mode.toModel()
}

fun MpBeContext.setWorkshopUpdateQuery(request: MpRequestWorkshopUpdate) = setRequest(request) {
    requestWorkshop = MpWorkshopModel (
        id = request.updateData?.id?.let { MpWorkshopIdModel(it) } ?: MpWorkshopIdModel.NONE,
        title = request.updateData?.title?: "",
        description = request.updateData?.description?: "",
        tagIds = request.updateData?.tagIds?.toMutableSet()?: mutableSetOf(),
    )
    stubCase = when (request.debug?.stubCase) {
        MpRequestWorkshopUpdate.StubCase.SUCCESS -> MpStubCase.ART_READ_SUCCESS
        else -> MpStubCase.NONE
    }
    workMode = request.debug?.mode.toModel()
}

fun MpBeContext.setWorkshopDeleteQuery(request: MpRequestWorkshopDelete) = setRequest(request) {
    this.requestWorkshopId = request.workshopId?.let { MpWorkshopIdModel(it) } ?: MpWorkshopIdModel.NONE
    stubCase = when (request.debug?.stubCase) {
        MpRequestWorkshopDelete.StubCase.SUCCESS -> MpStubCase.ART_READ_SUCCESS
        else -> MpStubCase.NONE
    }
    workMode = request.debug?.mode.toModel()
}

fun MpBeContext.setWorkshopListQuery(request: MpRequestWorkshopList) = setRequest(request) {
    this.requestWorkshopFilter = request.filterData?.let {
        MpWorkshopFilterModel(
            text = it.text?: ""
        )
    }?: MpWorkshopFilterModel.NONE
    stubCase = when (request.debug?.stubCase) {
        MpRequestWorkshopList.StubCase.SUCCESS -> MpStubCase.ART_READ_SUCCESS
        else -> MpStubCase.NONE
    }
    workMode = request.debug?.mode.toModel()
}

fun MpBeContext.respondWorkshopCreate() = MpResponseWorkshopCreate(
    workshop = responseWorkshop.takeIf { it != MpWorkshopModel.NONE }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    status = status.toTransport(),
    responseId = responseId,
    onRequest = onRequest,
    endTime = Instant.now().toString(),
    debug = MpResponseWorkshopCreate.Debug(
        mode = workMode.takeIf { it != MpWorkMode.DEFAULT }?.toTransport()
    )
)

fun MpBeContext.respondWorkshopRead() = MpResponseWorkshopRead(
    workshop = responseWorkshop.takeIf { it != MpWorkshopModel.NONE }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    status = status.toTransport(),
    responseId = responseId,
    onRequest = onRequest,
    endTime = Instant.now().toString(),
    debug = MpResponseWorkshopRead.Debug(
        mode = workMode.takeIf { it != MpWorkMode.DEFAULT }?.toTransport()
    )
)

fun MpBeContext.respondWorkshopUpdate() = MpResponseWorkshopUpdate(
    workshop = responseWorkshop.takeIf { it != MpWorkshopModel.NONE }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    status = status.toTransport(),
    responseId = responseId,
    onRequest = onRequest,
    endTime = Instant.now().toString(),
    debug = MpResponseWorkshopUpdate.Debug(
        mode = workMode.takeIf { it != MpWorkMode.DEFAULT }?.toTransport()
    )
)

fun MpBeContext.respondWorkshopDelete() = MpResponseWorkshopDelete(
    workshop = responseWorkshop.takeIf { it != MpWorkshopModel.NONE }?.toTransport(),
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    status = status.toTransport(),
    responseId = responseId,
    onRequest = onRequest,
    endTime = Instant.now().toString(),
    debug = MpResponseWorkshopDelete.Debug(
        mode = workMode.takeIf { it != MpWorkMode.DEFAULT }?.toTransport()
    )
)

fun MpBeContext.respondWorkshopList() = MpResponseWorkshopList(
    workshops = responseWorkshops.takeIf { it.isNotEmpty() }?.filter { it != MpWorkshopModel.NONE }
        ?.map { it.toTransport() },
    errors = errors.takeIf { it.isNotEmpty() }?.map { it.toTransport() },
    status = status.toTransport(),
    responseId = responseId,
    onRequest = onRequest,
    endTime = Instant.now().toString(),
    debug = MpResponseWorkshopList.Debug(
        mode = workMode.takeIf { it != MpWorkMode.DEFAULT }?.toTransport()
    )
)

internal fun MpWorkshopModel.toTransport() = MpWorkshopDto(
    id = id.id.takeIf { it.isNotBlank() },
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    tagIds = tagIds.takeIf { it.isNotEmpty() },
)

fun MpArtDto.toInternal() = MpArtModel(
    id = id?.let {MpArtIdModel(it)} ?: MpArtIdModel.NONE,
    title = title ?: "",
    description = description ?: "",
    tagIds = tagIds?.toMutableSet()?: mutableSetOf(),
)
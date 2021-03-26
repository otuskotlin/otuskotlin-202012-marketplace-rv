package ru.otus.otuskotlin.marketplace.mappers

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpWorkshopModel
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpArtDto
import ru.otus.otuskotlin.marketplace.transport.models.workshops.MpRequestWorkshopCreate
import ru.otus.otuskotlin.marketplace.transport.models.workshops.MpRequestWorkshopDelete
import ru.otus.otuskotlin.marketplace.transport.models.workshops.MpRequestWorkshopRead
import ru.otus.otuskotlin.marketplace.transport.models.workshops.MpRequestWorkshopUpdate

fun MpBeContext.setWorkshopCreateQuery(request: MpRequestWorkshopCreate) {
    requestWorkshop = MpWorkshopModel (
        title = request.createData?.title?: "",
        description = request.createData?.description?: "",
        tagIds = request.createData?.tagIds?.toMutableSet()?: mutableSetOf(),
    )
}

fun MpBeContext.setWorkshopReadQuery(request: MpRequestWorkshopRead) {
    this.requestWorkshopId = request.workshopId?.let { MpWorkshopIdModel(it) } ?: MpWorkshopIdModel.NONE
}

fun MpBeContext.setWorkshopUpdateQuery(request: MpRequestWorkshopUpdate) {
    requestWorkshop = MpWorkshopModel (
        id = request.updateData?.id?.let { MpWorkshopIdModel(it) } ?: MpWorkshopIdModel.NONE,
        title = request.updateData?.title?: "",
        description = request.updateData?.description?: "",
        tagIds = request.updateData?.tagIds?.toMutableSet()?: mutableSetOf(),
        arts = request.updateData?.arts?.map {it.toInternal()}?.toMutableSet() ?: mutableSetOf(),
    )
}

fun MpBeContext.setWorkshopDeleteQuery(request: MpRequestWorkshopDelete) {
    this.requestWorkshopId = request.workshopId?.let { MpWorkshopIdModel(it) } ?: MpWorkshopIdModel.NONE
}

fun MpArtDto.toInternal() = MpArtModel(
    id = id?.let {MpArtIdModel(it)} ?: MpArtIdModel.NONE,
    title = title ?: "",
    description = description ?: "",
    tagIds = tagIds?.toMutableSet()?: mutableSetOf(),
)
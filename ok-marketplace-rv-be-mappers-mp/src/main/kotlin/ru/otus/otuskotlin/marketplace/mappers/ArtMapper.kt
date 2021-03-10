package ru.otus.otuskotlin.marketplace.mappers

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtIdModel
import ru.otus.otuskotlin.marketplace.common.backend.models.MpArtModel
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpRequestArtCreate
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpRequestArtDelete
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpRequestArtRead
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpRequestArtUpdate

fun MpBeContext.setArtCreateQuery(request: MpRequestArtCreate) {
    requestArt = MpArtModel (
        title = request.createData?.title?: "",
        description = request.createData?.description?: "",
        tagIds = request.createData?.tagIds?.toMutableSet()?: mutableSetOf(),
        )
}

fun MpBeContext.setArtReadQuery(request: MpRequestArtRead) {
    this.requestArtId = request.artId?.let { MpArtIdModel(it) } ?: MpArtIdModel.NONE
}

fun MpBeContext.setArtUpdateQuery(request: MpRequestArtUpdate) {
    requestArt = MpArtModel (
        id = request.updateData?.id?.let {MpArtIdModel(it)} ?: MpArtIdModel.NONE,
        title = request.updateData?.title?: "",
        description = request.updateData?.description?: "",
        tagIds = request.updateData?.tagIds?.toMutableSet()?: mutableSetOf(),
    )
}

fun MpBeContext.setArtDeleteQuery(request: MpRequestArtDelete) {
    this.requestArtId = request.artId?.let { MpArtIdModel(it) } ?: MpArtIdModel.NONE
}
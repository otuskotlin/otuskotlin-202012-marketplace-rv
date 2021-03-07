package ru.otus.otuskotlin.marketplace.mappers

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpRequestArtCreate
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpRequestArtDelete
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpRequestArtRead
import ru.otus.otuskotlin.marketplace.transport.models.arts.MpRequestArtUpdate
import ru.otus.otuskotlin.marketplace.transport.models.common.IMpRequest
import ru.otus.otuskotlin.marketplace.transport.models.workshops.MpRequestWorkshopCreate
import ru.otus.otuskotlin.marketplace.transport.models.workshops.MpRequestWorkshopDelete
import ru.otus.otuskotlin.marketplace.transport.models.workshops.MpRequestWorkshopRead
import ru.otus.otuskotlin.marketplace.transport.models.workshops.MpRequestWorkshopUpdate

fun MpBeContext.setQuery(request: IMpRequest) {
    when {
        request is MpRequestArtCreate -> this.setArtCreateQuery (request)
        request is MpRequestArtRead -> this.setArtReadQuery (request)
        request is MpRequestArtUpdate -> this.setArtUpdateQuery (request)
        request is MpRequestArtDelete -> this.setArtDeleteQuery (request)
        request is MpRequestWorkshopCreate -> this.setWorkshopCreateQuery (request)
        request is MpRequestWorkshopRead -> this.setWorkshopReadQuery (request)
        request is MpRequestWorkshopUpdate -> this.setWorkshopUpdateQuery (request)
        request is MpRequestWorkshopDelete -> this.setWorkshopDeleteQuery (request)
    }
}
package ru.otus.otuskotlin.marketplace.mappers

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.transport.models.arts.*
import ru.otus.otuskotlin.marketplace.transport.models.common.IMpRequest
import ru.otus.otuskotlin.marketplace.transport.models.workshops.*

fun MpBeContext.setRequest(request: IMpRequest) {
    when (request) {
        is MpRequestArtCreate -> this.setArtCreateQuery (request)
        is MpRequestArtRead -> this.setArtReadQuery (request)
        is MpRequestArtUpdate -> this.setArtUpdateQuery (request)
        is MpRequestArtDelete -> this.setArtDeleteQuery (request)
        is MpRequestArtList -> this.setArtListQuery (request)
        is MpRequestWorkshopCreate -> this.setWorkshopCreateQuery (request)
        is MpRequestWorkshopRead -> this.setWorkshopReadQuery (request)
        is MpRequestWorkshopUpdate -> this.setWorkshopUpdateQuery (request)
        is MpRequestWorkshopDelete -> this.setWorkshopDeleteQuery (request)
        is MpRequestWorkshopList -> this.setWorkshopListQuery (request)
    }
}
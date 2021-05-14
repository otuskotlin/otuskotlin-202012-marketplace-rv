package ru.otus.otuskotlin.services

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.mappers.*
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.ArtCrud
import ru.otus.otuskotlin.marketplace.transport.models.arts.*

class ArtService(private val crud: ArtCrud) {

    suspend fun create(context: MpBeContext, query: MpRequestArtCreate?): MpResponseArtCreate = with (context) {
        query?.also { setQuery(it) }
        crud.create(this)
        return respondArtCreate()
    }

    suspend fun read(context: MpBeContext, query: MpRequestArtRead?): MpResponseArtRead = with (context) {
        query?.also { setQuery(it) }
        crud.read(this)
        return respondArtRead()
    }

    suspend fun update(context: MpBeContext, query: MpRequestArtUpdate?): MpResponseArtUpdate = with (context) {
        query?.also { setQuery(it) }
        crud.update(this)
        return respondArtUpdate()
    }

    suspend fun delete(context: MpBeContext, query: MpRequestArtDelete?): MpResponseArtDelete = with (context) {
        query?.also { setQuery(it) }
        crud.delete(this)
        return respondArtDelete()
    }

    suspend fun list(context: MpBeContext, query: MpRequestArtList?): MpResponseArtList = with (context) {
        query?.also { setQuery(it) }
        crud.filter(this)
        return respondArtList()
    }
}

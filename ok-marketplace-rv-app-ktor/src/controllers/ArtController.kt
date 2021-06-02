package ru.otus.otuskotlin.controllers

import io.ktor.routing.*
import ru.otus.otuskotlin.marketplace.mappers.*
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.ArtCrud
import ru.otus.otuskotlin.marketplace.transport.models.arts.*

fun Routing.artRouting(crud: ArtCrud) {
    post(RestEndpoints.artCreate) {
        handleRoute<MpRequestArtCreate, MpResponseArtCreate> { query ->
            query?.also { setRequest(it) }
            crud.create(this)
            respondArtCreate()
        }
    }
    post(RestEndpoints.artRead) {
        handleRoute<MpRequestArtRead, MpResponseArtRead> { query ->
            query?.also { setRequest(it) }
            crud.read(this)
            respondArtRead()
        }
    }
    post(RestEndpoints.artUpdate) {
        handleRoute<MpRequestArtUpdate, MpResponseArtUpdate> { query ->
            query?.also { setRequest(it) }
            crud.update(this)
            respondArtUpdate()
        }
    }
    post(RestEndpoints.artDelete) {
        handleRoute<MpRequestArtDelete, MpResponseArtDelete> { query ->
            query?.also { setRequest(it) }
            crud.delete(this)
            respondArtDelete()
        }
    }
    post(RestEndpoints.artList) {
        handleRoute<MpRequestArtList, MpResponseArtList> { query ->
            query?.also { setRequest(it) }
            crud.filter(this)
            respondArtList()
        }
    }
}
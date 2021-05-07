package ru.otus.otuskotlin.controllers

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.marketplace.mappers.*
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.ArtCrud
import ru.otus.otuskotlin.marketplace.transport.models.arts.*
import ru.otus.otuskotlin.marketplace.transport.models.common.MpErrorDto
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.marketplace.transport.models.common.MpResponseStatusDto

fun Routing.artRouting(crud: ArtCrud) {
    post(RestEndpoints.artCreate) {
        handleRoute<MpRequestArtCreate, MpResponseArtCreate> { query ->
            query?.also { setQuery(it) }
            crud.create(this)
            respondArtCreate()
        }
    }
    post(RestEndpoints.artRead) {
        handleRoute<MpRequestArtRead, MpResponseArtRead> { query ->
            query?.also { setQuery(it) }
            crud.read(this)
            respondArtRead()
        }
    }
    post(RestEndpoints.artUpdate) {
        handleRoute<MpRequestArtUpdate, MpResponseArtUpdate> { query ->
            query?.also { setQuery(it) }
            crud.update(this)
            respondArtUpdate()
        }
    }
    post(RestEndpoints.artDelete) {
        handleRoute<MpRequestArtDelete, MpResponseArtDelete> { query ->
            query?.also { setQuery(it) }
            crud.delete(this)
            respondArtDelete()
        }
    }
    post(RestEndpoints.artList) {
        handleRoute<MpRequestArtList, MpResponseArtList> { query ->
            query?.also { setQuery(it) }
            crud.filter(this)
            respondArtList()
        }
    }
}
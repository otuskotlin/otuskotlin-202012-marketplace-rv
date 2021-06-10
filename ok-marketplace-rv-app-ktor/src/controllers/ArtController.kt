package ru.otus.otuskotlin.controllers

import io.ktor.routing.*
import ru.otus.otuskotlin.marketplace.mappers.*
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.ArtCrud
import ru.otus.otuskotlin.marketplace.transport.models.arts.*
import ru.otus.otuskotlin.services.ArtService

fun Routing.artRouting(service: ArtService) {
    post(RestEndpoints.artCreate) {
        handleRoute<MpRequestArtCreate, MpResponseArtCreate> { query ->
            service.create(this, query)
        }
    }
    post(RestEndpoints.artRead) {
        handleRoute<MpRequestArtRead, MpResponseArtRead> { query ->
            service.read(this, query)
        }
    }
    post(RestEndpoints.artUpdate) {
        handleRoute<MpRequestArtUpdate, MpResponseArtUpdate> { query ->
            service.update(this, query)
        }
    }
    post(RestEndpoints.artDelete) {
        handleRoute<MpRequestArtDelete, MpResponseArtDelete> { query ->
            service.delete(this, query)
        }
    }
    post(RestEndpoints.artList) {
        handleRoute<MpRequestArtList, MpResponseArtList> { query ->
            service.list(this, query)
        }
    }
}
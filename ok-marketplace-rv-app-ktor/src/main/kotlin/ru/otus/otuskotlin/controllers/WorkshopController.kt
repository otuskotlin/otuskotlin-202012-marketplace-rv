package ru.otus.otuskotlin.controllers

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*
import org.slf4j.LoggerFactory
import ru.otus.otuskotlin.marketplace.mappers.*
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.ArtCrud
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.WorkshopCrud
import ru.otus.otuskotlin.marketplace.transport.models.arts.*
import ru.otus.otuskotlin.marketplace.transport.models.common.MpErrorDto
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.marketplace.transport.models.common.MpResponseStatusDto
import ru.otus.otuskotlin.marketplace.transport.models.workshops.*

fun Routing.workshopRouting(crud: WorkshopCrud) {
    post(RestEndpoints.workshopCreate) {
        handleRoute<MpRequestWorkshopCreate, MpResponseWorkshopCreate> { query ->
            query?.also { setQuery(it) }
            crud.create(this)
            respondWorkshopCreate()
        }
    }
    post(RestEndpoints.workshopRead) {
        handleRoute<MpRequestWorkshopRead, MpResponseWorkshopRead> { query ->
            query?.also { setQuery(it) }
            crud.read(this)
            respondWorkshopRead()
        }
    }
    post(RestEndpoints.workshopUpdate) {
        handleRoute<MpRequestWorkshopUpdate, MpResponseWorkshopUpdate> { query ->
            query?.also { setQuery(it) }
            crud.update(this)
            respondWorkshopUpdate()
        }
    }
    post(RestEndpoints.workshopDelete) {
        handleRoute<MpRequestWorkshopDelete, MpResponseWorkshopDelete> { query ->
            query?.also { setQuery(it) }
            crud.delete(this)
            respondWorkshopDelete()
        }
    }
    post(RestEndpoints.workshopList) {
        handleRoute<MpRequestWorkshopList, MpResponseWorkshopList> { query ->
            query?.also { setQuery(it) }
            crud.filter(this)
            respondWorkshopList()
        }
    }
}
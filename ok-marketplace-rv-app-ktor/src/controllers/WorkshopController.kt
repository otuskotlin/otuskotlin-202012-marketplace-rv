package ru.otus.otuskotlin.controllers

import io.ktor.routing.*
import ru.otus.otuskotlin.marketplace.mappers.*
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.WorkshopCrud
import ru.otus.otuskotlin.marketplace.transport.models.workshops.*

fun Routing.workshopRouting(crud: WorkshopCrud) {
    post(RestEndpoints.workshopCreate) {
        handleRoute<MpRequestWorkshopCreate, MpResponseWorkshopCreate> { query ->
            query?.also { setRequest(it) }
            crud.create(this)
            respondWorkshopCreate()
        }
    }
    post(RestEndpoints.workshopRead) {
        handleRoute<MpRequestWorkshopRead, MpResponseWorkshopRead> { query ->
            query?.also { setRequest(it) }
            crud.read(this)
            respondWorkshopRead()
        }
    }
    post(RestEndpoints.workshopUpdate) {
        handleRoute<MpRequestWorkshopUpdate, MpResponseWorkshopUpdate> { query ->
            query?.also { setRequest(it) }
            crud.update(this)
            respondWorkshopUpdate()
        }
    }
    post(RestEndpoints.workshopDelete) {
        handleRoute<MpRequestWorkshopDelete, MpResponseWorkshopDelete> { query ->
            query?.also { setRequest(it) }
            crud.delete(this)
            respondWorkshopDelete()
        }
    }
    post(RestEndpoints.workshopList) {
        handleRoute<MpRequestWorkshopList, MpResponseWorkshopList> { query ->
            query?.also { setRequest(it) }
            crud.filter(this)
            respondWorkshopList()
        }
    }
}
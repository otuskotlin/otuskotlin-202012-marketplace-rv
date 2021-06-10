package ru.otus.otuskotlin.controllers

import io.ktor.routing.*
import ru.otus.otuskotlin.marketplace.mappers.*
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.WorkshopCrud
import ru.otus.otuskotlin.marketplace.transport.models.workshops.*
import ru.otus.otuskotlin.services.WorkshopService

fun Routing.workshopRouting(service: WorkshopService) {
    post(RestEndpoints.workshopCreate) {
        handleRoute<MpRequestWorkshopCreate, MpResponseWorkshopCreate> { query ->
            service.create(this, query)
        }
    }
    post(RestEndpoints.workshopRead) {
        handleRoute<MpRequestWorkshopRead, MpResponseWorkshopRead> { query ->
            service.read(this, query)
        }
    }
    post(RestEndpoints.workshopUpdate) {
        handleRoute<MpRequestWorkshopUpdate, MpResponseWorkshopUpdate> { query ->
            service.update(this, query)
        }
    }
    post(RestEndpoints.workshopDelete) {
        handleRoute<MpRequestWorkshopDelete, MpResponseWorkshopDelete> { query ->
            service.delete(this, query)
        }
    }
    post(RestEndpoints.workshopList) {
        handleRoute<MpRequestWorkshopList, MpResponseWorkshopList> { query ->
            service.list(this, query)
        }
    }
}
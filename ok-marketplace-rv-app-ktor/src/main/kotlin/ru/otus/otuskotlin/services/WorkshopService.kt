package ru.otus.otuskotlin.services

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.mappers.*
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.WorkshopCrud
import ru.otus.otuskotlin.marketplace.transport.models.workshops.*

class WorkshopService(private val crud: WorkshopCrud) {

    suspend fun create(context: MpBeContext, query: MpRequestWorkshopCreate?): MpResponseWorkshopCreate = with (context) {
        query?.also { setQuery(it) }
        crud.create(this)
        return respondWorkshopCreate()
    }

    suspend fun read(context: MpBeContext, query: MpRequestWorkshopRead?): MpResponseWorkshopRead = with (context) {
        query?.also { setQuery(it) }
        crud.read(this)
        return respondWorkshopRead()
    }

    suspend fun update(context: MpBeContext, query: MpRequestWorkshopUpdate?): MpResponseWorkshopUpdate = with (context) {
        query?.also { setQuery(it) }
        crud.update(this)
        return respondWorkshopUpdate()
    }

    suspend fun delete(context: MpBeContext, query: MpRequestWorkshopDelete?): MpResponseWorkshopDelete = with (context) {
        query?.also { setQuery(it) }
        crud.delete(this)
        return respondWorkshopDelete()
    }

    suspend fun list(context: MpBeContext, query: MpRequestWorkshopList?): MpResponseWorkshopList = with (context) {
        query?.also { setQuery(it) }
        crud.filter(this)
        return respondWorkshopList()
    }
}

package ru.otus.otuskotlin.helpers

import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.transport.models.arts.*
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.marketplace.transport.models.workshops.*
import ru.otus.otuskotlin.services.ArtService
import ru.otus.otuskotlin.services.WorkshopService


suspend fun service(
    context: MpBeContext,
    query: MpMessage?,
    artService: ArtService,
    workshopService: WorkshopService,
): MpMessage = when(query) {
    is MpRequestArtList ->   artService.list(context, query)
    is MpRequestArtCreate -> artService.create(context, query)
    is MpRequestArtRead ->   artService.read(context, query)
    is MpRequestArtUpdate -> artService.update(context, query)
    is MpRequestArtDelete -> artService.delete(context, query)

    is MpRequestWorkshopList ->   workshopService.list(context, query)
    is MpRequestWorkshopCreate -> workshopService.create(context, query)
    is MpRequestWorkshopRead ->   workshopService.read(context, query)
    is MpRequestWorkshopUpdate -> workshopService.update(context, query)
    is MpRequestWorkshopDelete -> workshopService.delete(context, query)

    else -> throw RuntimeException("It's somehow unreal to be here")
    
}

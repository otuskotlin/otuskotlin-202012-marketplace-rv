package ru.otus.otuskotlin.controllers

import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import ru.otus.otuskotlin.helpers.service
import ru.otus.otuskotlin.jsonConfig
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContextStatus
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.services.ArtService
import ru.otus.otuskotlin.services.WorkshopService
import ru.otus.otuskotlin.toModel
import java.time.Instant
import java.util.*

@OptIn(InternalSerializationApi::class)
fun Routing.mpWebsocket(
    artService: ArtService,
    workshopService: WorkshopService
) {
    webSocket("/ws") { // websocketSession
        for (frame in incoming) {
            if (frame is Frame.Text) {
                val ctx = MpBeContext(
                    responseId = UUID.randomUUID().toString(),
                    timeStarted = Instant.now()
                )
                try {
                    val requestJson = frame.readText()
                    val query = jsonConfig.decodeFromString(MpMessage.serializer(), requestJson)
                    ctx.status = MpBeContextStatus.RUNNING
                    val response = service(
                        context = ctx,
                        query = query,
                        artService = artService,
                        workshopService = workshopService
                    )
                    val respJson = jsonConfig.encodeToString(MpMessage::class.serializer(), response)
                    outgoing.send(Frame.Text(respJson))
                } catch (e: Throwable) {
                    ctx.status = MpBeContextStatus.FAILING
                    ctx.errors.add(e.toModel())
                    val response = service(
                        context = ctx,
                        query = null,
                        artService = artService,
                        workshopService = workshopService
                    )
                    val respJson = jsonConfig.encodeToString(MpMessage::class.serializer(), response)
                    outgoing.send(Frame.Text(respJson))
                }
            }
        }
    }
}

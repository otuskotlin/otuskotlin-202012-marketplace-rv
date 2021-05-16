package ru.otus.otuskotlin.controllers

import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import ru.otus.otuskotlin.helpers.WsUserSession
import ru.otus.otuskotlin.helpers.service
import ru.otus.otuskotlin.jsonConfig
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContextStatus
import ru.otus.otuskotlin.marketplace.common.backend.repositories.EmptyUserSession
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.services.ArtService
import ru.otus.otuskotlin.services.WorkshopService
import ru.otus.otuskotlin.toModel
import java.time.Instant
import java.util.*

private val sessions = mutableMapOf<WebSocketSession, WsUserSession>()

@OptIn(InternalSerializationApi::class)
fun Routing.mpWebsocket(
    artService: ArtService,
    workshopService: WorkshopService
) {
    webSocket("/ws") { // websocketSession
        sessions[this] = WsUserSession(fwSession = this)
        apply {
            val ctx = MpBeContext(
                responseId = UUID.randomUUID().toString(),
                timeStarted = Instant.now(),
                userSession = sessions[this] ?: EmptyUserSession,
                status = MpBeContextStatus.RUNNING
            )
            service(
                context = ctx,
                query = null,
                artService = artService,
                workshopService = workshopService
            )?.also {
                val respJson = jsonConfig.encodeToString(MpMessage::class.serializer(), it)
                outgoing.send(Frame.Text(respJson))
            }
        }

        for (frame in incoming) {
            if (frame is Frame.Text) {
                val ctx = MpBeContext(
                    responseId = UUID.randomUUID().toString(),
                    timeStarted = Instant.now(),
                    userSession = sessions[this] ?: EmptyUserSession
                )
                try {
                    val requestJson = frame.readText()
                    val query = jsonConfig.decodeFromString(MpMessage.serializer(), requestJson)
                    ctx.status = MpBeContextStatus.RUNNING
                    service(
                        context = ctx,
                        query = query,
                        artService = artService,
                        workshopService = workshopService
                    )?.also {
                        val respJson = jsonConfig.encodeToString(MpMessage::class.serializer(), it)
                        outgoing.send(Frame.Text(respJson))
                    }
                } catch (e: ClosedReceiveChannelException) {
                    service(
                        context = ctx,
                        query = null,
                        artService = artService,
                        workshopService = workshopService
                    )
                    sessions -= this
                } catch (e: Throwable) {
                    e.printStackTrace()
                    ctx.status = MpBeContextStatus.FAILING
                    ctx.errors.add(e.toModel())
                    service(
                        context = ctx,
                        query = null,
                        artService = artService,
                        workshopService = workshopService
                    )?.also {
                        val respJson = jsonConfig.encodeToString(MpMessage::class.serializer(), it)
                        outgoing.send(Frame.Text(respJson))
                    }
                }
            }
        }
    }
}

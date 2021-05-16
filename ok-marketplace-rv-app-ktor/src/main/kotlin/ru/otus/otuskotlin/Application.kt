package ru.otus.otuskotlin

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import kotlinx.serialization.InternalSerializationApi
import ru.otus.otuskotlin.controllers.artRouting
import ru.otus.otuskotlin.controllers.mpWebsocket
import ru.otus.otuskotlin.controllers.workshopRouting
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.ArtCrud
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.WorkshopCrud
import ru.otus.otuskotlin.services.ArtService
import ru.otus.otuskotlin.services.WorkshopService

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@OptIn(InternalSerializationApi::class)
@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val artCrud = ArtCrud()
    val workshopCrud = WorkshopCrud()
    val artService = ArtService(artCrud)
    val workshopService = WorkshopService(workshopCrud)

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(WebSockets)
    install(ContentNegotiation) {
        json(
            contentType = ContentType.Application.Json,
            json = jsonConfig,
        )
    }

    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        routing {
            // Static feature. Try to access `/static/ktor_logo.svg`
            static("/static") {
                resources("static")
            }

        artRouting(artCrud)
        workshopRouting(workshopCrud)

            mpWebsocket(artService, workshopService)
        }
    }
}

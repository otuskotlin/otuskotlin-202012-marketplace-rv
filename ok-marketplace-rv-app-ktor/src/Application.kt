package ru.otus.otuskotlin

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import rabbitMqEndpoints
import ru.otus.otuskotlin.controllers.artRouting
import ru.otus.otuskotlin.controllers.websocketEndpoints
import ru.otus.otuskotlin.controllers.workshopRouting
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.arts.ArtRepoInMemory
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.workshops.WorkshopRepoInMemory
import ru.otus.otuskotlin.marketplace.common.backend.repositories.IArtRepository
import ru.otus.otuskotlin.marketplace.common.backend.repositories.IWorkshopRepository
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.ArtCrud
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.WorkshopCrud
import ru.otus.otuskotlin.services.ArtService
import ru.otus.otuskotlin.services.WorkshopService
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@OptIn(ExperimentalTime::class)
@Suppress("unused") // Referenced in application.conf
fun Application.module(
    testing: Boolean = false,
    testArtRepo: IArtRepository? = null,
    testWorkshopRepo: IWorkshopRepository? = null,

    ) {

    val artRepoTest = ArtRepoInMemory(ttl = 2.toDuration(DurationUnit.HOURS))
    val workshopRepoTest = WorkshopRepoInMemory(ttl = 2.toDuration(DurationUnit.HOURS))
    val artCrud = ArtCrud(
        artRepoTest = artRepoTest,
        workshopRepoTest = workshopRepoTest,
    )
    val workshopCrud = WorkshopCrud(
        workshopRepoTest = workshopRepoTest,
        artRepoTest = artRepoTest,
    )

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

    install(ContentNegotiation) {
        json(
            contentType = ContentType.Application.Json,
            json = jsonConfig,
        )
    }

    websocketEndpoints(
        artService = artService,
        workshopService = workshopService
    )

    val rabbitMqEndpoint = environment.config.propertyOrNull("marketplace.rabbitmq.endpoint")?.getString()
    if (rabbitMqEndpoint != null) {
        rabbitMqEndpoints(
            rabbitMqEndpoint = rabbitMqEndpoint,
            artService = artService,
            workshopService = workshopService
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

        artRouting(artService)
        workshopRouting(workshopService)

        }
    }
}

package ru.otus.otuskotlin

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.features.*
import io.ktor.serialization.*
import ru.otus.otuskotlin.controllers.artRouting
import ru.otus.otuskotlin.controllers.workshopRouting
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.ArtCrud
import ru.otus.otuskotlin.marketplace.rv.business.logic.backend.WorkshopCrud

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    val artCrud = ArtCrud()
    val workshopCrud = WorkshopCrud()

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
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

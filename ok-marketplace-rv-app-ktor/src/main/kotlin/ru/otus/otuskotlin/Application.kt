package ru.otus.otuskotlin

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.features.*
import io.ktor.serialization.*
import ru.otus.otuskotlin.controllers.ArtController
import ru.otus.otuskotlin.controllers.WorkshopController

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
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

    val artController = ArtController()
    val workshopController = WorkshopController()

    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        route("/art") {
            post("/create") {
                artController.create(this)
            }
            post("/read") {
                artController.read(this)
            }
            post("/update") {
                artController.update(this)
            }
            post("/delete") {
                artController.delete(this)
            }
        }
        route("/workshop") {
            post("/create") {
                workshopController.create(this)
            }
            post("/read") {
                workshopController.read(this)
            }
            post("/update") {
                workshopController.update(this)
            }
            post("/delete") {
                workshopController.delete(this)
            }
        }
    }

    routing {
        // Static feature. Try to access `/static/ktor_logo.svg`
        static("/static") {
            resources("static")
        }


    }
    routing {
        install(StatusPages) {
            exception<AuthenticationException> { cause ->
                call.respond(HttpStatusCode.Unauthorized)
            }
            exception<AuthorizationException> { cause ->
                call.respond(HttpStatusCode.Forbidden)
            }

        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()

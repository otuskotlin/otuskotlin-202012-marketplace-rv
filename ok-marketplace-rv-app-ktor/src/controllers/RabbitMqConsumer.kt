package ru.otus.otuskotlin.controllers

import io.ktor.application.*
import kotlinx.coroutines.runBlocking
import pl.jutupe.ktor_rabbitmq.consume
import pl.jutupe.ktor_rabbitmq.publish
import pl.jutupe.ktor_rabbitmq.rabbitConsumer
import ru.otus.otuskotlin.helpers.service
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContext
import ru.otus.otuskotlin.marketplace.common.backend.context.MpBeContextStatus
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.services.ArtService
import ru.otus.otuskotlin.services.WorkshopService
import ru.otus.otuskotlin.toModel
import java.time.Instant
import java.util.*

fun Application.rabbitMq(
    queueIn: String,
    exchangeOut: String,
    artService: ArtService,
    workshopService: WorkshopService
) {
    rabbitConsumer {
        consume<MpMessage>(queueIn) { consumerTag, query ->
            println("Consumed message $query, consumer tag: $consumerTag")
            val ctx = MpBeContext(
                responseId = UUID.randomUUID().toString(),
                timeStarted = Instant.now(),
            )
            try {
                ctx.status = MpBeContextStatus.RUNNING
                runBlocking {
                    service(
                        context = ctx,
                        query = query,
                        artService = artService,
                        workshopService = workshopService
                    )?.also {
                        publish(exchangeOut, "", null, it)
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                ctx.status = MpBeContextStatus.FAILING
                ctx.errors.add(e.toModel())
                runBlocking {
                    service(
                        context = ctx,
                        query = null,
                        artService = artService,
                        workshopService = workshopService
                    )?.also {
                        publish(exchangeOut, "", null, it)
                    }
                }
            }
        }
    }
}

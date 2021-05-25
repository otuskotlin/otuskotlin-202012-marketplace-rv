import io.ktor.application.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import pl.jutupe.ktor_rabbitmq.RabbitMQ
import pl.jutupe.ktor_rabbitmq.consume
import pl.jutupe.ktor_rabbitmq.publish
import pl.jutupe.ktor_rabbitmq.rabbitConsumer
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
fun Application.rabbitMqEndpoints(
    rabbitMqEndpoint: String,
    artService: ArtService,
    workshopService: WorkshopService,
) {
    //val queueIn by lazy { environment.config.property("marketplace.rabbitmq.queueIn").getString() }
    //val exchangeIn by lazy { environment.config.property("marketplace.rabbitmq.exchangeIn").getString() }
    //val exchangeOut by lazy { environment.config.property("marketplace.rabbitmq.exchangeOut").getString() }

    val queueIn = "marketplaceQueueIn"
    val exchangeIn = "marketplaceExchangeIn"
    val exchangeOut = "marketplaceExchangeOut"

    install(RabbitMQ) {
        uri = rabbitMqEndpoint
        connectionName = "Connection name"

        //serialize and deserialize functions are required
        serialize {
            when (it) {
                is MpMessage -> jsonConfig.encodeToString(MpMessage.serializer(), it).toByteArray()
                else -> jsonConfig.encodeToString(Any::class.serializer(), it).toByteArray()
            }
        }
        deserialize { bytes, type ->
            val jsonString = String(bytes, Charsets.UTF_8)
            jsonConfig.decodeFromString(type.serializer(), jsonString)
        }

        //example initialization logic
        initialize {
            exchangeDeclare(exchangeIn, "fanout", true)
            exchangeDeclare(exchangeOut, "fanout", true)
            queueDeclare(queueIn, true, false, false, emptyMap())
            queueBind(
                queueIn,
                exchangeIn,
                "*"
            )
        }
    }
    rabbitMq(
        queueIn = queueIn,
        exchangeOut = exchangeOut,
        artService = artService,
        workshopService = workshopService
    )
}

fun Application.rabbitMq(
    queueIn: String,
    exchangeOut: String,
    artService: ArtService,
    workshopService: WorkshopService,
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
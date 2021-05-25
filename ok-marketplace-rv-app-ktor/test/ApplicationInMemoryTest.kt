import io.ktor.http.*
import io.ktor.server.testing.*
import ru.otus.otuskotlin.controllers.RestEndpoints
import ru.otus.otuskotlin.jsonConfig
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.arts.ArtRepoInMemory
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.workshops.WorkshopRepoInMemory
import ru.otus.otuskotlin.marketplace.common.backend.models.*
import ru.otus.otuskotlin.marketplace.transport.models.arts.*
import ru.otus.otuskotlin.marketplace.transport.models.common.MpMessage
import ru.otus.otuskotlin.marketplace.transport.models.common.MpWorkModeDto
import ru.otus.otuskotlin.marketplace.transport.models.common.ResponseStatusDto
import ru.otus.otuskotlin.module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.fail
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

@OptIn(ExperimentalTime::class)
internal class ApplicationInMemoryTest {
    companion object {
        val art1 = MpArtModel(
            id = MpArtIdModel("id13"),
            title = "art1",
            description = "art1 the first",
            )
        val art2 = MpArtModel(
            id = MpArtIdModel("id2"),
            title = "art2",
            description = "art2 the second",
            )
        val art3 = MpArtModel(
            id = MpArtIdModel("art3"),
            title = "art3",
            description = "art3 the third",
            )

        val workshop = MpWorkshopModel(
            id = MpWorkshopIdModel("workshop1"),
            title = "workshop"
        )

        val artRepo by lazy {
            ArtRepoInMemory(
                ttl = 15.toDuration(DurationUnit.MINUTES),
                initObjects = listOf(art1, art2, art3)
            )
        }

        val workshopRepo by lazy {
            WorkshopRepoInMemory(
                ttl = 15.toDuration(DurationUnit.MINUTES),
                initObjects = listOf(workshop)
            )
        }
    }

    @Test
    fun testRead() {
        withTestApplication({module(testArtRepo = artRepo)}) {
            handleRequest(HttpMethod.Post, RestEndpoints.artRead) {
                val body = MpRequestArtRead(
                    requestId = "12345",
                    artId = art1.id.id,
                    debug = MpRequestArtRead.Debug(mode = MpWorkModeDto.TEST)
                )

                val format = jsonConfig

                val bodyString = format.encodeToString(MpMessage.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = (jsonConfig.decodeFromString(MpMessage.serializer(), jsonString) as? MpResponseArtRead)
                    ?: fail("Incorrect response format")

                assertEquals(ResponseStatusDto.SUCCESS, res.status)
                assertEquals("12345", res.onRequest)
                assertEquals(art1.title, res.art?.title)
                assertEquals(art1.description, res.art?.description)
            }
        }
    }

    @Test
    fun testCreate() {
        withTestApplication({module(testArtRepo = artRepo)}) {
            handleRequest(HttpMethod.Post, RestEndpoints.artCreate) {
                val body = MpRequestArtCreate(
                    requestId = "12345",
                    createData = MpArtCreateDto(
                        title = art2.title,
                        description = art2.description,
                    ),
                    debug = MpRequestArtCreate.Debug(mode = MpWorkModeDto.TEST)
                )

                val format = jsonConfig

                val bodyString = format.encodeToString(MpMessage.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = (jsonConfig.decodeFromString(MpMessage.serializer(), jsonString) as? MpResponseArtCreate)
                    ?: fail("Incorrect response format")

                assertEquals(ResponseStatusDto.SUCCESS, res.status)
                assertEquals("12345", res.onRequest)
                assertEquals(art2.title, res.art?.title)
                assertEquals(art2.description, res.art?.description)
            }
        }
    }

    @Test
    fun testUpdate() {
        withTestApplication({module(testArtRepo = artRepo)}) {
            handleRequest(HttpMethod.Post, RestEndpoints.artUpdate) {
                val body = MpRequestArtUpdate(
                    requestId = "12345",
                    updateData = MpArtUpdateDto(
                      id = art3.id.id,
                      title = art3.title,
                      description = art3.description
                    ),
                    debug = MpRequestArtUpdate.Debug(mode = MpWorkModeDto.TEST)
                )

                val format = jsonConfig

                val bodyString = format.encodeToString(MpMessage.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = (jsonConfig.decodeFromString(MpMessage.serializer(), jsonString) as? MpResponseArtUpdate)
                    ?: fail("Incorrect response format")

                assertEquals(ResponseStatusDto.SUCCESS, res.status)
                assertEquals("12345", res.onRequest)
                assertEquals(art3.id.id, res.art?.id)
                assertEquals(art3.title, res.art?.title)
                assertEquals(art3.description, res.art?.description)
            }
        }
    }

    @Test
    fun testDelete() {
        withTestApplication({module(testArtRepo = artRepo)}) {
            handleRequest(HttpMethod.Post, RestEndpoints.artDelete) {
                val body = MpRequestArtDelete(
                    requestId = "12345",
                    artId = art2.id.id,
                    debug = MpRequestArtDelete.Debug(mode = MpWorkModeDto.TEST)
                )

                val format = jsonConfig

                val bodyString = format.encodeToString(MpMessage.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = (jsonConfig.decodeFromString(MpMessage.serializer(), jsonString) as? MpResponseArtDelete)
                    ?: fail("Incorrect response format")

                assertEquals(ResponseStatusDto.SUCCESS, res.status)
                assertEquals("12345", res.onRequest)
                assertEquals(art2.id.id, res.art?.id)
                assertEquals(art2.title, res.art?.title)
                assertEquals(art2.description, res.art?.description)
            }
        }
    }

    @Test
    fun testList() {
        withTestApplication({module(testArtRepo = artRepo)}) {
            handleRequest(HttpMethod.Post, RestEndpoints.artList) {
                val body = MpRequestArtList(
                    requestId = "12345",
                    filterData = MpArtFilterDto(
                        text = "the",
                        offset = 0,
                        count = 10,
                        includeDescription = true,
                    ),
                    debug = MpRequestArtList.Debug(mode = MpWorkModeDto.TEST)
                )

                val format = jsonConfig

                val bodyString = format.encodeToString(MpMessage.serializer(), body)
                setBody(bodyString)
                addHeader("Content-Type", "application/json")
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(ContentType.Application.Json.withCharset(Charsets.UTF_8), response.contentType())
                val jsonString = response.content ?: fail("Null response json")
                println(jsonString)

                val res = (jsonConfig.decodeFromString(MpMessage.serializer(), jsonString) as? MpResponseArtList)
                    ?: fail("Incorrect response format")

                assertEquals(ResponseStatusDto.SUCCESS, res.status)
                assertEquals("12345", res.onRequest)
                assertEquals(3, res.arts?.size)
                assertEquals(1, res.pageCount)
            }
        }
    }
}
